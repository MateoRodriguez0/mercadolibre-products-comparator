
# MercadoLibre products comparator

Compare MercadoLibre products, allows you to see the similarities and differences between the compared products. This app integrates with the MercadoLibre API and allows you to compare products using the urls of the publications, compares the information of the products, sellers, etc.

## authentication and authorization

In order to access the application resources, the Authentication and Authorization processes must be carried out. This way, you will be able to interact with the application on behalf of your MercadoLibre account.
To authenticate yourself you must register with your Mercadolibre account at the following url to obtain the authorization code.

````https
https://auth.mercadolibre.com.co/authorization?response_type=code&client_id=7688862085485252&redirect_uri=https://mercadolibrecomparator.com
````
It will redirect to the mercadolibre page to carry out the authentication process with the oauth 2.0 protocol
![Captura de pantalla 2024-02-09 222353](https://github.com/MateoRodriguez0/mercadolibre-products-comparator/assets/107595139/b192d65a-0594-437d-b36c-03a6b09878ed)

After registering or logging in with a MercadoLibre account, you will be giving the application permissions to make requests to the MercadoLibre API on behalf of the account owner.


## Documentation

[Spring Cloud Config](https://github.com/MateoRodriguez0/mercadolibre-products-comparator/blob/master/config-server/README.md)

[Discovery service whit Netflix eureka](https://github.com/MateoRodriguez0/mercadolibre-products-comparator/blob/master/eureka-server/README.md)

[Servicio-vendedores]()

[Servicio-analisis-url]()

