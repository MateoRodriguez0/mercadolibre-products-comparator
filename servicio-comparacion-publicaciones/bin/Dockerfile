FROM openjdk:21-slim-bullseye

WORKDIR /app

COPY ./target/servicio-comparacion-publicaciones-0.0.1.jar .


ENTRYPOINT [ "java","-jar","--enable-preview","servicio-comparacion-publicaciones-0.0.1.jar"]