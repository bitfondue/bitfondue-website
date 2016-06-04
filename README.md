# bitfondue

[![Circle CI](https://circleci.com/gh/bitfondue/bitfondue-website.svg?style=svg)](https://circleci.com/gh/bitfondue/bitfondue-website)

Another approach at keeping things you've found on the web.

## Development

### Figwheel (for the front-end)

1. `lein figwheel` will start a server, serving the files in `resources/public` and watching & recompiling any ClojureScript files.

### REPL

1. Copy the `.env.dist` file to `.env_dev` and fill in your custom settings.
1. Make sure you have foreman installed.
1. Start the development server with `foreman run -e .env_dev lein ring server-headless`

### Database

1. Make sure you have a PostgreSQL server running. `v9.4.1` or higher
1. Create a database for the project to use `psql -c "create database bitfondue_dev"`
1. `foreman run -e .env_dev lein migrate`

## Staging/production

### Application

1. `heroku create`
1. `heroku buildpacks:set https://github.com/ddollar/heroku-buildpack-multi.git`

### Database

1. Running the migrations `heroku run JVM_OPTS="" lein migrate --app your-app-name`
