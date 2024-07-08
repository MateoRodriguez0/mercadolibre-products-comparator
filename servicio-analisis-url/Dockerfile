FROM openjdk:21-slim-bullseye

WORKDIR /app

COPY ./target/servicio-analisis-url-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT [ "java","-jar","--enable-preview","servicio-analisis-url-0.0.1-SNAPSHOT.jar"]