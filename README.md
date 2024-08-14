# Compare Products

Compare products, allows you to see the similarities and differences between the compared products. This app integrates with the MercadoLibre API and allows you to compare products using the urls of the publications, compares the information of the products, sellers and other things.

[![website](https://github.com/user-attachments/assets/735244da-4813-4639-b02f-c601eb8dc99f)](https://www.compareproducts.site/)

## authentication and authorization

In order to access the application resources, the Authentication and Authorization processes must be carried out. In this way the user will be able to interact with the application in the name of their MercadoLibre account. To authenticate you must register with your Mercadolibre account and after everything is successful you will be redirected to the home page.

After registering or logging in with a MercadoLibre account, you will be giving the application permissions to make requests to the MercadoLibre API on behalf of the account owner.

## Integration with Google cloud vertex AI
[Generative AI](https://cloud.google.com/vertex-ai/generative-ai/docs/learn/overview) and natural language are the main technologies used to achieve the objective, which is to generate a value comparison from the information in the publications.

This application takes advantage of the benefits of [Vertex AI](https://cloud.google.com/vertex-ai/docs)'s generative AI to analyze the descriptions and characteristics of the products or publications that will be compared and through AI.
Generative Gemini can give guidance to users about which publication may be a better purchase option. AI is also used to make a comparison based on the opinions of a publication.

In the **compareproducts-AI-services** submodule you will find the services with analysis functionalities with generative AI.

The model currently used is **Gemini 1.0 Pro**.

## Documentation

[Servicio-vendedores]()

[Servicio-analisis-url]()

[Servicio-categorias]()


# performance

The application uses redis cache in the cloud and concurrent programming to optimize performance and make good use of system resources, in order to avoid bottlenecks when making some product comparisons.

## Recommendations
It is not recommended to clone this repository for some reasons:

- The application is dependent on some configurations that are not available in this  repository.
- you need to have a eureka server so that microservices can be discovered.
- This repository only contains a part of the logic, since it does not have the applications that make use of artificial intelligence, nor does it include the security layer.

# Architecture
![compareproducts-backend](https://github.com/user-attachments/assets/810c3f1d-c633-4c86-ab18-2ad59296ae75)
