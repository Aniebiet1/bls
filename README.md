Book Lending System (BLS)

Simple Java Swing desktop app backed by PostgreSQL.

Requirements

- Java 17+
- PostgreSQL 14+
- Postgres JDBC driver in lib/postgresql-<version>.jar (already present)

Compile

javac -d out src/\*_/_.java

Run

java -cp out:lib/postgresql-42.7.7.jar Main

Config
Edit `config/app.properties` to set `db.url`, `db.user`, and `db.password`.

DB
Create tables using the SQL from the design document.
