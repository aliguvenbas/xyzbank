FROM openjdk:17
MAINTAINER ag
COPY build/libs/xyzbank-*.jar xyzbank.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/xyzbank.jar"]
