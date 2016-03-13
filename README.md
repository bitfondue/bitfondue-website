# bitfondue

[![Circle CI](https://circleci.com/gh/bitfondue/bitfondue-website.svg?style=svg)](https://circleci.com/gh/bitfondue/bitfondue-website)
[![Docker Repository on Quay](https://quay.io/repository/bitfondue/bitfondue-website/status "Docker Repository on Quay")](https://quay.io/repository/bitfondue/bitfondue-website)
[![wercker status](https://app.wercker.com/status/5f5af9f094c5119e764ac5f429bff4b0/m "wercker status")](https://app.wercker.com/project/bykey/5f5af9f094c5119e764ac5f429bff4b0)

Another approach at keeping things you've found on the web.

## Development

### SSL Certificate

```bash
# create the certificate
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
-keyout bitfondue.dev.key -out bitfondue.dev.crt

# then add the crt file to your certificate manager (keychain access on OS X)
```

### Figwheel (for the front-end)

1. `lein figwheel` will start a server, serving the files in `resources/public` and watching & recompiling any ClojureScript files.

### REPL

1. Copy the `.env.dist` file to `.env_dev` and fill in your custom settings.
1. Make sure you have foreman installed.
1. Start the development repl with `foreman run -e .env_dev lein repl`
1. Start the web-server `(-main "3000")`

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
