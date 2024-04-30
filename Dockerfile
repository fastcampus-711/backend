FROM openjdk:17-alpine

WORKDIR /app
COPY build/libs/*.jar /app/spring.jar
COPY src/main/resources /app

EXPOSE 80

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "-Duser.timezone=Asia/Seoul", "spring.jar"]