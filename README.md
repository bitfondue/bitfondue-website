# bitfondue

Another approach at keeping things you've found on the web.

## Setup

### Database

1. Make sure you have a PostgreSQL server running. `v9.4.1` or higher
1. Create a database for the project to use `psql -c "create database bitfondue_dev"`
1. Load the schema into the database `psql -f src/bitfondue/sql/schema.sql -d bitfondue_dev`

### REPL

1. Copy the `.env.dist` file to `.env_dev` and fill in your custom settings.
1. Make sure you have foreman installed.
1. Start the development repl with `foreman run -e .env_dev lein repl`
