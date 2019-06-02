# Spring Boot with MongoDB
## What was build
Spring application for creating and retrieving entity stored in a MongoDB database using Spring Data REST

Also, it is leveraging Flapdoodle’s embedded MongoDB to run integration tests smoothly

## What you'll need
MongoDb

Java 1.8

Maven 3.0+

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

## Embedded Testing
It’s a good approach when we want to test if our application behaves accordingly when the code is working directly with the persistence layer

Unfortunately, using an embedded server cannot be considered as “full integration testing”; hence, if we want to run communication tests in the environment as close to the production as possible, a better solution is to use an environment container such as Docker
 

![Print](https://github.com/diogo-santos/spring-rest-mongodb/blob/master/spring-rest-mongodb.png)