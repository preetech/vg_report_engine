
[![License](https://img.shields.io/badge/license-Vanguard%202021-green)](https://img.shields.io/badge/license-Vanguard%202021-green)
[![Release](https://img.shields.io/badge/release-0.0.1-orange)](https://img.shields.io/badge/release-0.0.1-orange)
[![Coverage Status](src/test/resources/coverage.svg?sanitize=true)](src/test/resources/coverage.svg?sanitize=true)

# Vanguard Trade Reporting Engine

The objective of this service is to read a set of xml event files, extracts a set of elements (fields), filters the events based on a set of criteria, and reports the events in comma separated value (CSV) format. 

The Service is build using Spring boot as the base application, which will expose the GET generateReport Rest API. The API responds back with below response:
- No Event Files found
- No Records Found for Report
- Report file generated in path: src/main/resources/reports/Report7153016413878475389.csv

### Sample output in Postman
---
![Image](data/postman.png?raw=true)

### Running the Application
---
##### Running with JAR file
Follow Below Steps to run the Applicaiton:

- Build the project with ```mvn clean install ```
- Run the Jar created with ```java -jar target/reportengine-0.0.1-SNAPSHOT.jar``` to start the application.

##### Running with Docker

##### Docker Config

Docker file has been implemented to create the base image and deploy the application in Docker.

- Build the docker image with below command:
```
docker build --file="Dockerfile" --tag=reportengine:latest .
```
- Run the Docker image with below command:
```
docker run -it --rm --name my-running-app reportengine
```

### System Requirements
---
To use this project you will require the following technology installed on your machine:
- Apache Maven
- JDK8

### Main Technologies Used
---
- Java 8
- Spring Boot
- Spring Web

### Configuration: Application
---
There is 1 major properties to be defined to run the service:
1. report.event.file.path : Path where event files are placed to take as an input for report.
2. report.event.report.path : Path where report is generated.
```
report.event.file.path=src/main/resources/events/
report.event.report.path=src/main/resources/reports/
```

### Integration and Junit Test
---
- Integration Test has been written using **rest-assured** to test the generateReport API service and perform end to end testing.
- Junit has been written to cover unit testing. **Jacoco** has been implemented to generate the coverage report. 
  Report is generated in path: target/site/jacoco/index.html. Minimum coverage report of **80%** is maintained.
  
### Assumptions
---
For the sake of simplicity, it is safe to assume that
- Event files are placed in the given repository

