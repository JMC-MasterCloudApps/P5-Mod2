# Práctica 5. Autenticación y autorización

## Usage
### Java

```sh
docker run --rm -d -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=booksDB -p 3306:3306 --name mysql mysql:8.0.22
cd java/auth
mvn clean install
mvn spring-boot:run
```

## API Root URL

```sh
https://localhost:8443/api/v1/<resource>
```

- Listado libros . . . . **GET**. . . . `/books/`
- Detalle libro . . . . . **GET**. . . . `/books/{bookId}`
- Nuevo libro . . . . . .**POST** . . `/books/`
- Borrar libro . . . . . .**DELETE** `/books/{bookId}`
- Nuevo comentario **POST** . . `/books/{bookId}/comments/`
- Detalle comentario **GET** . . . `/books/{bookId}/comments/{commentId}`
- Borrar comentario **DELETE** . . `/books/{bookId}/comments/{commentId}`

## Author

👤 **Juan Miguel Cabrera**

* Github: [juanmi-gh](https://github.com/JMC-MasterCloudApps/P5-Mod2)

Note that you should have to [install JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html), [Maven](https://maven.apache.org/install.html) and [Docker](https://docs.docker.com/engine/install/) as prerequisite.
