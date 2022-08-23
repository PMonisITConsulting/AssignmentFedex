# AssignmentFedex

# Description

### What it does
This app provides an API that connects to another API in order to fetch information about tracking, shipments and prices.

### Technologies used
Other than the typical choices (Java, SpringBoot), the big choice here was WebFlux framework.

Spring WebFlux is used to create fully asynchronous and non-blocking application built on event-loop execution model, which fits perfectly for the
requirements of this application.

# How to Install and Run the Project

After having the external api running (port 8080):

### Docker

If using docker, it's necessary to create an internal network in order
to access the external api:
```
docker network create int-network
```
After this, we just need to run the image of the backend-services:
```
docker run --network "int-network" --name "backend-services" --publish 8000:8000 xyzassessment/backend-services:latest
```

As requested I didn't publish this assessment in any repository, so we'll have to build the image first.
```
docker build --tag assessment .
```
```
docker run --network "int-network" --name "aggregation-api" --publish 8081:8081 assessment
```
That's it! We are ready to start making some requests at http://localhost:8081/aggregation!

### Command Line
In order to run the app from the command line it's necessary
to have java 17 as default in Java_Home.

At the moment the application.yml has the internal network url for the external application,
so it can be used with docker. If you want to run the application directly from your 
IDE or int the command line, please change the following property in application.yml:
```
backEndServices:
  baseUri: http://localhost:8080/
```

If so, all that is necessary is:
```
mvn spring-boot:run
```

