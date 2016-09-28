# pokesearch

    A Rest api for finding pokémons!

Uses the [pokéapi](https://pokeapi.co/) to find pokémons caching
the resources as needed to abide by its fair use policy.

You can check the API documentation and test the endpoints [here](https://clj-pokesearch.herokuapp.com/docs)

## Requirements

You will need to have a JDK and leiningen installed to build and
run this application.

## Usage

The api supports two media types `application/json` and `application/vnd.api+json`

You should provide the `Accept` http header to indicate what you want to use.

#### Example

Search for **dragonite** using `application/json`.

    curl -X GET -H "Accept: application/json" "https://clj-pokesearch.herokuapp.com/api/dragonite"

Search for **pikachu** using `application/vnd.api+json`.

    curl -X GET -H "Accept: application/vnd.api+json" "https://clj-pokesearch.herokuapp.com/api/pikachu"

### Run the application locally

`lein ring server` or `lein run`

During development is better to run `lein ring server` because it supports
livereload.

### Running tests

`lein test` will run the test suite once and exit.

`lein test-refresh` will run the test suite every time a file changes or every time you hit enter on the console.

### Packaging and running as standalone jar

```
lein do clean, uberjar
java -jar target/server.jar
```
