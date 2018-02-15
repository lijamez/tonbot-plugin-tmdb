package net.tonbot.plugin.tmdb;

import javax.annotation.Nonnull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.tonbot.common.Param;

@EqualsAndHashCode
@ToString
public class MovieRequest {

	@Getter
	@Param(name = "movie name", ordinal = 0, description = "The movie name.", captureRemaining = true)
	@Nonnull
	private String movieName;
}
