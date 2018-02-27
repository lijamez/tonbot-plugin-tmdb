package net.tonbot.plugin.tmdb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
class Genre {
	
	private final int id;
	private final String name;
	
	@JsonCreator
	public Genre(
			@JsonProperty("id") int id, 
			@JsonProperty("name") String name) {
		this.id = id;
		this.name = name;
	}
}
