FROM unitfinance/jdk17-sbt-scala

COPY ./build/libs/danim_be-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
