FROM openjdk:17
MAINTAINER ag
COPY build/libs/xyzbank-*.jar xyzbank.jar
ENTRYPOINT ["java","-jar","/xyzbank.jar"]
