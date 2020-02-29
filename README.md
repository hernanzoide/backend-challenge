# backend-challenge
## About the solution

Spring boot REST API to solve backend challenge. Application class is _DemoApplication.java_ . Added swagger for manual test avaialble at: http://localhost:8080/swagger-ui.html#!/

Used postgreSQL docker image. Command to run it:

> docker run --name my_postgres -e POSTGRES_PASSWORD=postgres -p 54320:5432 -d postgres:latest

Added JUnit tests in _DemoApplicationTests.java_ for:

- Simple reservation creation and retrieval. 
- Multiple thread reservation creation.
- Checking for availability maximun.




