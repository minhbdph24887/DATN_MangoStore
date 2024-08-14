FROM openjdk:21
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} datn-mangostore-app.jar
ENTRYPOINT ["java", "-jar", "/datn-mangostore-app.jar"]
