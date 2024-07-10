FROM openjdk:21-slim-bullseye

WORKDIR /app

COPY ./target/servicio-productos-comparacion-especificaciones-0.0.1-SNAPSHOT.jar .


ENTRYPOINT [ "java","-jar","--enable-preview","servicio-productos-comparacion-especificaciones-0.0.1-SNAPSHOT.jar"]