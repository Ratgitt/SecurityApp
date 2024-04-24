FROM openjdk:17-jdk

WORKDIR /app

COPY ./target/SecurityOWASPWithEmailVerification-0.0.1-SNAPSHOT.jar /app/SecurityOwasp.jar

EXPOSE 8080

CMD ["java", "-jar", "SecurityOwasp.jar"]