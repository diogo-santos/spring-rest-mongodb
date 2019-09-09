# Spring Boot with MongoDB
![Print](https://github.com/diogo-santos/spring-rest-mongodb/blob/master/api-rest-mongodb.png)

Spring application for creating and retrieving entity stored in a MongoDB database using Spring Data REST

## What you'll need
MongoDB

Java 1.8

Maven 3.0+

[Docker](https://www.linkedin.com/pulse/install-docker-ubuntu-1804-diogo-santos/)


## Instructions
Import the project from GitHub

Run spring-rest-mongodb app
```
mvn spring-boot:run
```
## Test the App
Now that the app is running, visit http://localhost:8080/people in order to see all the RESTful endpoints 


## MongoDB Repository
Spring Data Repository interface allow you to perform various operations related to the Person Object. It gets these operations by extending MongoRepository, which in turn extends the PagingAndSortingRepository interface defined in Spring Data Commons

At runtime, Spring Data REST will create an implementation of this interface automatically

## Integration Test
Run test.sh script
```
./test.sh
```
If we want to run communication tests in the environment as close to the production as possible, a better solution is to use an environment container such as Docker. You can learn more by checking out this article: 

[Integration tests using Docker](https://www.linkedin.com/pulse/integration-tests-using-docker-diogo-santos)
