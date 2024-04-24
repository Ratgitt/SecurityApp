#FROM openjdk:17-jdk
#
#WORKDIR /app
#
#COPY ./target/SecurityOWASPWithEmailVerification-0.0.1-SNAPSHOT.jar /app/SecurityOwasp.jar
#
#EXPOSE 8080
#
#CMD ["java", "-jar", "SecurityOwasp.jar"]

FROM openjdk:17-jdk as builder
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install -DskipTests

FROM openjdk:17-jdk
WORKDIR /opt/app
EXPOSE 8080
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod", "-jar", "/opt/app/*.jar" ]