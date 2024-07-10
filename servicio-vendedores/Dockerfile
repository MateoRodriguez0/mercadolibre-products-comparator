FROM openjdk:21-slim-bullseye

WORKDIR /app

COPY ./target/servicio-vendedores-0.0.1-SNAPSHOT.jar .


ENTRYPOINT [ "java","-jar","--enable-preview","servicio-vendedores-0.0.1-SNAPSHOT.jar"]