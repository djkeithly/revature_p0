# Bank repl application

This is a simple bank account system run inside of the terminal. This application is hooked up to a docker file which contains a postgresql database (implementation inside of the ERD in the documents directory).

This is a Java project, designed to be built in Maven using postgres and docker. Testing uses Junit 5.

## Setup instructions

Backend Built through docker using this command:

```wsl
docker run --name repl-bank-db \
  -v repl-bank-postgres-data:/var/lib/postgresql \
  -e POSTGRES_PASSWORD=root \
  -e POSTGRES_DB=repl-bank \
  -p 5432:5432 \
  -d postgres
```

Once this command is run a db.properties will need to be made inside of the java/com/revature/resources directory using the corresponding information.

To compile:

```bash
mvn clean compile
```

To run the application:

```bash
mvn exec:java '-Dexec.mainClass=com.revature.api.Main'
```
