# Tonbot TMDb Plugin [![Build Status](https://travis-ci.org/lijamez/tonbot-plugin-tmdb.svg?branch=master)](https://travis-ci.org/lijamez/tonbot-plugin-tmdb)

Look up movie and TV show information from [The Movie DB (TMDb)](https://www.themoviedb.org).

## Installation
* Add ``net.tonbot.plugin.tmdb.TMDbPlugin`` to your Tonbot plugins config.
* [Create a TMDb account](https://www.themoviedb.org/account/signup) and get your API key (v3 auth) from https://www.themoviedb.org/settings/api
* Put your API key into ``{TONBOT CONFIG DIR}/plugin_config/net.tonbot.plugin.tmdb.TMDbPlugin.config``.

Example config:
```json
{
  "tmdbApiKey" : "YOUR API KEY HERE"
}
```

## Usage

Look up movie info:
```
t, movie The Room
```

Look up TV show info:
```
t, tv Game of Thrones
```

## Acknowledgements
![TMDb](https://www.themoviedb.org/assets/static_cache/bb45549239e25f1770d5f76727bcd7c0/images/v4/logos/408x161-powered-by-rectangle-blue.png)
