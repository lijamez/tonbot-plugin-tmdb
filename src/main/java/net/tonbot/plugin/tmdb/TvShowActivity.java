package net.tonbot.plugin.tmdb;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import net.tonbot.common.Activity;
import net.tonbot.common.ActivityDescriptor;
import net.tonbot.common.BotUtils;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

public class TvShowActivity implements Activity {

	private static final ActivityDescriptor ACTIVITY_DESCRIPTOR = ActivityDescriptor.builder()
			.route(ImmutableList.of("tv"))
			.parameters(ImmutableList.of("tv show name"))
			.description("Gets information about a TV show.")
			.build();

	private static final DecimalFormat RATING_FORMAT = new DecimalFormat("#.#");
	private static final String TMDB_TV_URL_ROOT = "https://www.themoviedb.org/tv/";

	private final TMDbClient tmdbClient;

	@Inject
	public TvShowActivity(TMDbClient tmdbClient) {
		this.tmdbClient = Preconditions.checkNotNull(tmdbClient, "tmdbClient must be non-null.");
	}

	@Override
	public ActivityDescriptor getDescriptor() {
		return ACTIVITY_DESCRIPTOR;
	}

	@Override
	public void enact(MessageReceivedEvent event, String query) {
		
		TvShowSearchResult result = this.tmdbClient.searchTvShows(query);
		
		if (result.getHits().size() > 0) {
			TvShowHit topHit = result.getHits().get(0);

			TvShow tvShow = tmdbClient.getTvShow(topHit.getId());

			EmbedObject embedObj = createEmbed(tvShow);

			BotUtils.sendEmbeddedContent(event.getChannel(), embedObj);
		} else {
			BotUtils.sendMessage(event.getChannel(), "I couldn't find a TV show with that name. :shrug:");
		}
	}
	
	private EmbedObject createEmbed(TvShow tvShow) {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.withTitle(tvShow.getName());

		embedBuilder.withUrl(TMDB_TV_URL_ROOT + tvShow.getId());

		if (!StringUtils.isBlank(tvShow.getOverview())) {
			embedBuilder.withDescription(tvShow.getOverview());
		}

		embedBuilder.appendField("Air Dates", tvShow.getFirstAirDate() + " to " + tvShow.getLastAirDate(), true);

		String ratingStr = tvShow.getVoteCount() > 0 ? RATING_FORMAT.format(tvShow.getVoteAverage()) + "/10"
				: "No rating";
		embedBuilder.appendField("Rating", ratingStr, true);
		embedBuilder.appendField("# of Episodes", Integer.toString(tvShow.getNumberOfEpisodes()), true);
		embedBuilder.appendField("# of Seasons", Integer.toString(tvShow.getNumberOfSeasons()), true);

		List<String> genreNames = tvShow.getGenres().stream()
				.map(Genre::getName)
				.filter(name -> !name.isEmpty())
				.collect(Collectors.toList());

		String genreNamesStr = genreNames.isEmpty() ? "Unknown" : StringUtils.join(genreNames, "\n");

		embedBuilder.appendField("Genres", genreNamesStr, true);

		if (tvShow.getPosterPath().isPresent()) {
			String imageUrl = tmdbClient.getImageUrl(tvShow.getPosterPath().get());
			embedBuilder.withImage(imageUrl);
		}

		embedBuilder.withFooterText("Powered by The Movie Database");
		
		return embedBuilder.build();
	}
}
