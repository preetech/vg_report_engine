#########################################################################################################
# Docker File to build the base image for running the Application on docker.							#
#########################################################################################################

#Use Alpine with Java 8 container as base
FROM openjdk:8-alpine

# Application Name
ENV APPNAME="reportengine"

# Set Jar file Home directory
ENV JAR_FILE_HOME=/opt/vg/${APPNAME}

# Copy the JAR file to apline base image
COPY target/*.jar ${JAR_FILE_HOME}/lib/app.jar

# Use Below EnteryPoint to run the docker container and testing to prove the image is running successfully.
ENTRYPOINT ["java", "-jar", "/opt/vg/reportengine/lib/app.jar"]

