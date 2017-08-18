package net.tonbot.plugin.tmdb;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
class MovieSearchResult {

	private final int page;
	private final int totalResults;
	private final int totalPages;
	private final List<MovieHit> hits;

	@JsonCreator
	public MovieSearchResult(
			@JsonProperty("page") int page,
			@JsonProperty("total_results") int totalResults,
			@JsonProperty("total_pages") int totalPages,
			@JsonProperty("results") List<MovieHit> hits) {
		this.page = page;
		this.totalResults = totalResults;
		this.totalPages = totalPages;
		this.hits = hits;
	}
}
