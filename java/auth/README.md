# Practice 5. Autenticación y autorización

## Usage

```sh
docker run --rm -d -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=booksDB -p 3306:3306 --name mysql mysql:8.0.22
mvn spring-boot:run
```

## API Root URL

```sh
http://localhost:8080/api/v1/<resource>
```

## Author

👤 **Juan Miguel Cabrera**

* Github: [juanmi-gh](https://github.com/JMC-MasterCloudApps/P5-Mod2)

Note that you should have to [install JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html), [Maven](https://maven.apache.org/install.html) and [Docker](https://docs.docker.com/engine/install/) as prerequisite.
