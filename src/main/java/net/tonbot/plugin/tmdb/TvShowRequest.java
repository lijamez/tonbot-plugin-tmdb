package net.tonbot.plugin.tmdb;

import javax.annotation.Nonnull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.tonbot.common.Param;

@EqualsAndHashCode
@ToString
class TvShowRequest {

	@Getter
	@Param(name = "tv show name", ordinal = 0, description = "The TV show name.", captureRemaining = true)
	@Nonnull
	private String tvShowName;
}
