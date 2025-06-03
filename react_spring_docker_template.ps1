$dirName = 'recruitment_task'
$projectName = 'LocalNews'

$frontendDir = "${dirName}_frontend"


# * Backend
Set-Location ..

spring init `
    -l=java `
    -d=web `
    --build maven `
    --name=$projectName `
    --force `
    $dirName

Set-Location $dirName

$controllersPath = "src/main/java/com/example/$dirName/controllers"

mkdir $controllersPath

# language=java
"package com.example.$dirName.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Suppress(`"unused`")
@Controller
class AppController {
    @RequestMapping(`"/`") String app() {
        return `"index.html`";
    }
}" > "$controllersPath/AppController.java"


# * Frontend

npx create-react-app $frontendDir

# * Docker

# language=Dockerfile
"FROM maven:3.8.4-openjdk-17 AS mvn-builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


FROM node:lts AS react-builder

WORKDIR /app
COPY $frontendDir .

RUN npm run build


FROM openjdk:17-jdk
LABEL authors='JanUf'

WORKDIR /app
COPY --from=mvn-builder app/target/*-0.0.1-SNAPSHOT.jar ./app.jar
COPY src/main/resources/application.properties ./config/application.properties
COPY --from=react-builder /app/build ./static

EXPOSE 8080
" > Dockerfile

# language=Dockerfile
'CMD ["java", "-jar", "app.jar"]' >> Dockerfile