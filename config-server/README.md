# centralized configuration service
The centralized configuration service implements the Spring Cloud Config dependency, this service will store some configuration properties for the other services in the application.
Use the private repository to store the configuration files of the other services.
````https
  https://github.com/MateoRodriguez0/mercadolibre-products-comparator-config.git
````


## Security 
The security in this application is configured with spring security with HTTP Basic authentication by defining the credentials in the application.yml.

````yml
spring:
  security:
    user:
      name: Productcomparatorserviceconfig
      password: client12345
````


## discovery 
registers as DiscoveryClient on a eureka server that in the url http://localhost:8761/eureka.
