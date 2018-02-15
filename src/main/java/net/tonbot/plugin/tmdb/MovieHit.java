package net.tonbot.plugin.tmdb;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
class MovieHit {

	private final int voteCount;
	private final int id;
	private final boolean video;
	private final double voteAverage;
	private final String title;
	private final double popularity;
	private final String posterPath;
	private final String originalLanguage;
	private final String originalTitle;
	private final List<Integer> genreIds;
	private final String backdropPath;
	private final boolean adult;
	private final String overview;
	private final String releaseDate;

	@JsonCreator
	public MovieHit(@JsonProperty("vote_count") int voteCount, @JsonProperty("id") int id,
			@JsonProperty("video") boolean video, @JsonProperty("vote_average") double voteAverage,
			@JsonProperty("title") String title, @JsonProperty("popularity") double popularity,
			@JsonProperty("poster_path") String posterPath, @JsonProperty("original_language") String originalLanguage,
			@JsonProperty("original_title") String originalTitle, @JsonProperty("genre_ids") List<Integer> genreIds,
			@JsonProperty("backdrop_path") String backdropPath, @JsonProperty("adult") boolean adult,
			@JsonProperty("overview") String overview, @JsonProperty("release_date") String releaseDate) {
		this.voteCount = voteCount;
		this.id = id;
		this.video = video;
		this.voteAverage = voteAverage;
		this.title = title;
		this.popularity = popularity;
		this.posterPath = posterPath;
		this.originalLanguage = originalLanguage;
		this.originalTitle = originalTitle;
		this.genreIds = genreIds;
		this.backdropPath = backdropPath;
		this.adult = adult;
		this.overview = overview;
		this.releaseDate = releaseDate;
	}

	public Optional<String> getBackdropPath() {
		return Optional.ofNullable(backdropPath);
	}

	public Optional<String> getPosterPath() {
		return Optional.ofNullable(posterPath);
	}

	public Optional<String> getOverview() {
		return Optional.ofNullable(overview);
	}
}
