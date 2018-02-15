package net.tonbot.plugin.tmdb;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * https://developers.themoviedb.org/3/search/search-tv-shows
 */
@Data
class TvShowSearchResult {

	private final int page;
	private final List<TvShowHit> hits;
	private final int totalResults;
	private final int totalPages;

	@JsonCreator
	public TvShowSearchResult(@JsonProperty("page") int page, @JsonProperty("results") List<TvShowHit> hits,
			@JsonProperty("total_results") int totalResults, @JsonProperty("total_pages") int totalPages) {
		this.page = page;
		this.hits = hits;
		this.totalResults = totalResults;
		this.totalPages = totalPages;
	}
}
