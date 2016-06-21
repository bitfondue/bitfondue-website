install: 	## Install Clojure dependencies
					@echo "Installing Clojure dependencies"
					@npm install
					@lein deps

start:	## Start a development server
				@echo "Starting a development server"
				@./node_modules/.bin/nf --procfile Procfile_dev -e .env_dev start --ssl-key bitfondue.dev.key --ssl-cert bitfondue.dev.crt -x 5001

.PHONY: help

help:
			@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

.DEFAULT_GOAL := help