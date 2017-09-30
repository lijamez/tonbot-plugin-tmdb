package net.tonbot.plugin.tmdb;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import net.tonbot.common.Activity;
import net.tonbot.common.TonbotPlugin;
import net.tonbot.common.TonbotPluginArgs;

public class TMDbPlugin extends TonbotPlugin {

	private Injector injector;

	public TMDbPlugin(TonbotPluginArgs args) {
		super(args);

		File configFile = args.getConfigFile();
		if (!configFile.exists()) {
			// TODO Create it.
			throw new IllegalStateException("Config file doesn't exist.");
		}

		ObjectMapper objectMapper = new ObjectMapper();

		try {
			Config config = objectMapper.readValue(configFile, Config.class);
			this.injector = Guice.createInjector(new TMDbModule(args.getPrefix(), args.getBotUtils(), config.getTmdbApiKey()));
		} catch (IOException e) {
			throw new RuntimeException("Could not read configuration file.", e);
		}

	}

	@Override
	public Set<Activity> getActivities() {
		return injector.getInstance(Key.get(new TypeLiteral<Set<Activity>>() {
		}));
	}

	@Override
	public String getFriendlyName() {
		return "Movie Info";
	}

	@Override
	public String getActionDescription() {
		return "Tell You About Movies and TV Shows";
	}
}
