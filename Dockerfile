FROM openjdk:21
ARG FILE_JAR=gradle/wrapper/gradle-wrapper.jar
ADD ${FILE_JAR} api-service.jar
ENTRYPOINT ["java", "-jar", "api-service.jar"]
EXPOSE 8080