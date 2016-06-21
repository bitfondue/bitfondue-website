# bitfondue

[![Circle CI](https://circleci.com/gh/bitfondue/bitfondue-website.svg?style=svg)](https://circleci.com/gh/bitfondue/bitfondue-website)

Another approach at keeping things you've found on the web.

## Development setup

### Self-Signed SSL Certificate (Instructions for OS X)

I decided that bitfondue should always be served via HTTPS and in order to prevent bugs being introduced because I develop without encryption you have to create a self-signed certificate in order to get a fully working development setup.

```bash
# Create a certificate in the project directory.
# When it comes to "Common Name (e.g. server FQDN or YOUR name) []:" give it the value "bitfondue.dev"
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout bitfondue.dev.key -out bitfondue.dev.crt

# Open the certificate in the Keychain Access application
open /Applications/Utilities/Keychain\ Access.app bitfondue.dev.crt

# In the "Category" section in the lower left go to "Certificates"
# Double-click the "bitfondue.dev" certificate and open the "Trust" section
# Change the value for "When using this certificate" to "Always Trust"
# Close the dialog and accept the change by entering your password
```

### Figwheel (for the front-end)

1. `lein figwheel` will start a server, serving the files in `resources/public` and watching & recompiling any ClojureScript files.

### REPL

1. Copy the `.env.dist` file to `.env_dev` and fill in your custom settings.
1. Install all dependencies with `make install`
1. Start the development server with `make start`

### Database

1. Make sure you have a PostgreSQL server running. `v9.4.1` or higher
1. Create a database for the project to use `psql -c "create database bitfondue_dev"`
1. `./node_modules/.bin/nf run -e .env_dev lein migrate`

## Staging/production

### Application

1. `heroku create`
1. `heroku buildpacks:set https://github.com/ddollar/heroku-buildpack-multi.git`

### Database

1. Running the migrations `heroku run JVM_OPTS="" lein migrate --app your-app-name`
