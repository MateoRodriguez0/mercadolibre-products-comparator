# Spring cloud Discovery server 

This is the registration and service discovery server in the application. Since this is an application made in Spring, it uses the architecture based on microservices. We will use the Netflix discovery services and specifically this is a Eureka server and it is available in the resource http://localhost8761/eureka so that the other microservices can register as clients.


## Security HttpBasic

the netflix eureka discovery server will be protected with spring security so that only authenticated services can register with the credentials

````yml
spring:
  security:
    user:
      name: eurekaclient 
      password: eurekaclient
````
