# Catalog App
This springboot app contains a REST API with CRUD endpoints to manage a localizable catalog of products.

### Install app
1. The command below will perform a clean build.
    ```
    ./gradlew clean build
    ```

### Run app

1. The command below will start the app on port 8080.
    ```
    ./gradlew bootRun
    ```
1. Use [Postman](https://www.postman.com/) to open the CatalogWebApp.postman_collection.json collection and test the 
CRUD endpoints.

### Next steps
### Test Coverage
1. Improve unit test coverage

### Security
1. The endpoints in this API are wide open for testing purposes only. In a Production system they will need to be 
secured.

### DB
1. An H2 DB is used for demo purposes, this would not be an good solution for an API in Production.

### Error handling
1. For the sake of simplicity, exceptions are handled in for each individual endpoint. In a Production system it would 
be much better to have a global error handling mechanism (e.g. using an error handler provided by the 
[@ControllerAdvice](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/ControllerAdvice.html) 
annotation).



