FROM adoptopenjdk/openjdk17
LABEL authors="rkfcl"

ENTRYPOINT ["java", "-jar", "app.jar"]

COPY ./build/libs/danim_be-0.0.1-SNAPSHOT.jar app.jar
