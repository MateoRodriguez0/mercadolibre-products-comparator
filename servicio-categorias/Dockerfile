FROM openjdk:21-slim-bullseye

WORKDIR /app

COPY ./target/servicio-categorias-0.0.1-SNAPSHOT.jar .


ENTRYPOINT [ "java","-jar","--enable-preview","servicio-categorias-0.0.1-SNAPSHOT.jar"]