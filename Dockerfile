FROM openjdk:8-jdk-alpine
COPY ChatServer/target/ChatServer-1.0-jar-with-dependencies.jar /home/spring/app.jar
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ENTRYPOINT ["java","-jar","/home/spring/app.jar","3000"]
EXPOSE 3000