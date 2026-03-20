FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/SkaetWalletSystem.jar app.jar

EXPOSE ${APP_PORT}

ENTRYPOINT ["java","-jar","app.jar"]