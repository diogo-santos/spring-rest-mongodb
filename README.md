# Spring Boot with MongoDB
## What was build
Spring application for creating and retrieving objects stored in a MongoDB database using Spring Data REST

## What you'll need
MongoDb

Java 1.8

Maven 3.0+

## Instructions
Import the project from GitHub

Run spring-rest-mongodb app
```
mvn clean package && java -jar target/spring-rest-mongo-0.0.1.jar
```
## Test the App
Now that the app is running, visit http://localhost:8080/people in order to see all the RESTful endpoints 


## MongoDB Repository
Spring Data Repository interface allow you to perform various operations related to the Person Object. It gets these operations by extending MongoRepository, which in turn extends the PagingAndSortingRepository interface defined in Spring Data Commons.

At runtime, Spring Data REST will create an implementation of this interface automatically.
