package net.tonbot.plugin.tmdb;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import lombok.Data;
import lombok.NonNull;
import net.tonbot.common.Activity;
import net.tonbot.common.ActivityDescriptor;
import net.tonbot.common.ActivityUsageException;
import net.tonbot.common.BotUtils;
import net.tonbot.common.Enactable;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

class MovieActivity implements Activity {

	private static final ActivityDescriptor ACTIVITY_DESCRIPTOR = ActivityDescriptor.builder().route("movie")
			.parameters(ImmutableList.of("<movie name>")).description("Gets information about a movie.").build();

	private static final Pattern YEAR_PATTERN = Pattern.compile("\\((?<year>[0-9]{4})\\)");

	private static final DecimalFormat RATING_FORMAT = new DecimalFormat("#.#");
	private static final String TMDB_MOVIE_URL_ROOT = "https://www.themoviedb.org/movie/";

	private final BotUtils botUtils;
	private final TMDbClient tmdbClient;

	@Inject
	public MovieActivity(BotUtils botUtils, TMDbClient tmdbClient) {
		this.botUtils = Preconditions.checkNotNull(botUtils, "botUtils must be non-null.");
		this.tmdbClient = Preconditions.checkNotNull(tmdbClient, "tmdbClient must be non-null.");
	}

	@Override
	public ActivityDescriptor getDescriptor() {
		return ACTIVITY_DESCRIPTOR;
	}

	@Enactable
	public void enact(MessageReceivedEvent event, MovieRequest request) {
		MovieSearchQuery msq = parseInput(request.getMovieName());
		MovieSearchResult result = this.tmdbClient.searchMovies(msq.getMovieName(), msq.getYear());

		if (result.getHits().size() > 0) {
			MovieHit topHit = result.getHits().get(0);

			Movie movie = tmdbClient.getMovie(topHit.getId());

			EmbedObject embedObj = createEmbed(movie);

			botUtils.sendEmbed(event.getChannel(), embedObj);
		} else {
			botUtils.sendMessage(event.getChannel(), "I couldn't find a movie with that name. :shrug:");
		}
	}

	private MovieSearchQuery parseInput(String input) {
		if (StringUtils.isBlank(input)) {
			throw new ActivityUsageException("You need to enter movie name.");
		}

		Matcher matcher = YEAR_PATTERN.matcher(input);

		String movieName;
		String year = null;
		if (matcher.find()) {
			year = matcher.group("year");
			movieName = matcher.replaceFirst("");
		} else {
			movieName = input;
		}

		return new MovieSearchQuery(movieName, year);
	}

	private EmbedObject createEmbed(Movie movie) {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.withTitle(movie.getTitle());

		embedBuilder.withUrl(TMDB_MOVIE_URL_ROOT + movie.getId());

		List<String> descriptionComponents = new ArrayList<>();

		String tagline = movie.getTagline().orElse(null);
		if (!StringUtils.isBlank(tagline)) {
			descriptionComponents.add("*" + movie.getTagline().get() + "*");
		}

		String overview = movie.getOverview().orElse(null);
		if (!StringUtils.isBlank(overview)) {
			descriptionComponents.add(movie.getOverview().get());
		}

		String description = StringUtils.join(descriptionComponents, "\n\n");
		if (!StringUtils.isBlank(description)) {
			embedBuilder.withDescription(description);
		}

		embedBuilder.appendField("Release Date", movie.getReleaseDate(), true);

		String ratingStr = movie.getVoteCount() > 0 ? RATING_FORMAT.format(movie.getVoteAverage()) + "/10"
				: "No rating";
		embedBuilder.appendField("Rating", ratingStr, true);

		List<String> genreNames = movie.getGenres().stream().map(Genre::getName).filter(name -> !name.isEmpty())
				.collect(Collectors.toList());

		String genreNamesStr = genreNames.isEmpty() ? "Unknown" : StringUtils.join(genreNames, "\n");

		embedBuilder.appendField("Genres", genreNamesStr, true);

		if (movie.getPosterPath().isPresent()) {
			String imageUrl = tmdbClient.getImageUrl(movie.getPosterPath().get());
			embedBuilder.withImage(imageUrl);
		}

		embedBuilder.withFooterText("Powered by The Movie Database");

		return embedBuilder.build();
	}

	@Data
	private static class MovieSearchQuery {

		@NonNull
		private final String movieName;
		private final String year;
	}

}
