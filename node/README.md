# Práctica 5: Autenticación y autorización

## Ejecución

```sh
$ docker run --rm -d -p 27017-27019:27017-27019 --name mongodb mongo
$ npm install
$ npm start
```
## API URL

```sh
http://localhost:3000/api/v1/<resource>
```

## Uso de la API

Se proporciona una colección Postman para interactuar con la API.

La baseURL tiene que configurarse para localhost:3000

La base de datos se inicializa con datos de ejemplo