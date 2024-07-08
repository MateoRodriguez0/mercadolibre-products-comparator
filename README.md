
# Compare Products

Compare products, allows you to see the similarities and differences between the compared products. This app integrates with the MercadoLibre API and allows you to compare products using the urls of the publications, compares the information of the products, sellers and other things.

## authentication and authorization

In order to access the application resources, the Authentication and Authorization processes must be carried out. This way, you will be able to interact with the application on behalf of your MercadoLibre account.
To authenticate yourself you must register with your Mercadolibre account at the following url to obtain the authorization code.


It will redirect to the mercadolibre page to carry out the authentication process with the oauth 2.0 protocol
![Captura de pantalla 2024-02-09 222353](https://github.com/MateoRodriguez0/mercadolibre-products-comparator/assets/107595139/b192d65a-0594-437d-b36c-03a6b09878ed)

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

