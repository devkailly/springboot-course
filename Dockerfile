FROM openjdk:19-jdk-alpine
EXPOSE 8080
ADD target/springbootessentials.jar springbootessentials.jar
ENTRYPOINT ["java", "-jar", "/springbootessentials.jar"]

