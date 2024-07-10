FROM openjdk:21-slim-bullseye

WORKDIR /app

COPY ./target/servicio-productos-opiniones-0.0.1-SNAPSHOT.jar .


ENTRYPOINT [ "java","-jar","--enable-preview","servicio-productos-opiniones-0.0.1-SNAPSHOT.jar"]