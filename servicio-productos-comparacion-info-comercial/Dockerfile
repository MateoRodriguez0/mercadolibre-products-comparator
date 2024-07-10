FROM openjdk:21-slim-bullseye

WORKDIR /app

COPY ./target/servicio-productos-comparacion-info-comercial-0.0.1-SNAPSHOT.jar .


ENTRYPOINT [ "java","-jar","--enable-preview","servicio-productos-comparacion-info-comercial-0.0.1-SNAPSHOT.jar"]