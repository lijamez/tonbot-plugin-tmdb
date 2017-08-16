package net.tonbot.plugin.tmdb

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

import net.tonbot.plugin.tmdb.Movie
import net.tonbot.plugin.tmdb.MovieSearchResult
import net.tonbot.plugin.tmdb.TMDbClient
import net.tonbot.plugin.tmdb.TvShow
import net.tonbot.plugin.tmdb.TvShowSearchResult

import org.apache.http.client.HttpClient
import org.apache.http.impl.client.HttpClients
import spock.lang.Specification

class TMDbClientIntegrationTest extends Specification {

	TMDbClient client

	def setup() {
		String tmdbApiKey = System.getProperty("tmdbApiKey");
		HttpClient httpClient  = HttpClients.createDefault();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		this.client = new TMDbClient(httpClient, objectMapper, tmdbApiKey);
	}

	def "search for movies"() {
		when:
		MovieSearchResult searchResult = this.client.searchMovies("kubo")

		then:
		searchResult != null
		!searchResult.getResults().isEmpty()
	}

	def "get a particular movie"() {
		when:
		Movie movie = this.client.getMovie(24428)

		then:
		movie != null
		movie.getTitle() == "The Avengers"
	}

	def "search for tv shows"() {
		when:
		TvShowSearchResult searchResult = this.client.searchTvShows("stranger things")

		then:
		searchResult != null
		!searchResult.getResults().isEmpty()
	}

	def "get a particular tv show"() {
		when:
		TvShow tvShow = this.client.getTvShow(66732)

		then:
		tvShow != null
		tvShow.getName() == "Stranger Things"
	}
}
