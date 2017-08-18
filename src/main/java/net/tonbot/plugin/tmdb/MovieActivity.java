package net.tonbot.plugin.tmdb;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import net.tonbot.common.Activity;
import net.tonbot.common.ActivityDescriptor;
import net.tonbot.common.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

public class MovieActivity implements Activity {

	private static final ActivityDescriptor ACTIVITY_DESCRIPTOR = ActivityDescriptor.builder()
			.route(ImmutableList.of("movie"))
			.parameters(ImmutableList.of("movie name"))
			.description("Gets information about a movie.")
			.build();

	private static DecimalFormat RATING_FORMAT = new DecimalFormat("#.#");
	private static final String TMDB_TV_URL_ROOT = "https://www.themoviedb.org/tv/";

	private final TMDbClient tmdbClient;

	@Inject
	public MovieActivity(TMDbClient tmdbClient) {
		this.tmdbClient = Preconditions.checkNotNull(tmdbClient, "tmdbClient must be non-null.");
	}

	@Override
	public ActivityDescriptor getDescriptor() {
		return ACTIVITY_DESCRIPTOR;
	}

	@Override
	public void enact(MessageReceivedEvent event, String query) {
		MovieSearchResult result = this.tmdbClient.searchMovies(query);
		if (result.getResults().size() > 0) {
			MovieSearchResult.Movie topMatch = result.getResults().get(0);

			Movie movie = tmdbClient.getMovie(topMatch.getId());

			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.withTitle(movie.getTitle());

			embedBuilder.withUrl(TMDB_TV_URL_ROOT + movie.getId());

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

			List<String> genreNames = movie.getGenres().stream()
					.map(Genre::getName)
					.filter(name -> !name.isEmpty())
					.collect(Collectors.toList());

			String genreNamesStr = genreNames.isEmpty() ? "Unknown" : StringUtils.join(genreNames, "\n");

			embedBuilder.appendField("Genres", genreNamesStr, true);

			if (movie.getPosterPath().isPresent()) {
				String imageUrl = tmdbClient.getImageUrl(movie.getPosterPath().get());
				embedBuilder.withImage(imageUrl);
			}

			embedBuilder.withFooterText("Powered by The Movie Database");

			BotUtils.sendEmbeddedContent(event.getChannel(), embedBuilder.build());
		} else {
			BotUtils.sendMessage(event.getChannel(), "I couldn't find a movie with that name. :shrug:");
		}
	}

}
