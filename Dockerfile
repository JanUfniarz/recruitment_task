FROM maven:3.8.4-openjdk-17 AS mvn-builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


FROM node:lts AS react-builder

WORKDIR /app
COPY recruitment_task_frontend .

RUN npm run build


FROM openjdk:17-jdk
LABEL authors='JanUf'

WORKDIR /app
COPY --from=mvn-builder app/target/*-0.0.1-SNAPSHOT.jar ./app.jar
COPY src/main/resources/application.properties ./config/application.properties
COPY --from=react-builder /app/build ./static

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
