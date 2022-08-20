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
If Docker is available, runnign the following commands will suffice:

As requested I didn't publish this assessment in any repository, so we'll have to build the image first.
```
docker build --tag assessment .
```
```
docker run --publish 8000:8000 assessment
```
That's it! We are ready to start making some requests!

### Command Line
In order to run the app from the command line it's necessary
to have java 17 as default in Java_Home.

If so, all that is necessary is:
```
mvn spring-boot:run
```

