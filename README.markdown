[![Build Status](https://travis-ci.org/testinfected/simple-petstore.png)](https://travis-ci.org/testinfected/simple-petstore)

# About

A fork of my [petstore](https://github.com/testinfected/petstore) repository, but re-built with simple tools and no IoC container.

It uses the following tools:

- [Simple](http://www.simpleframework.org/), an embeddable high-performance HTTP server
- [Molecule](https://github.com/testinfected/molecule), a micro framework for web development 
- [Mustache](http://mustache.github.com) for logic-less templating
- Plain JDBC
- No IoC container
- [Gradle](http://www.gradle.org) for the build system 

## Preparing

To prepare the development and test databases in MySQL:

```sql
create database petstore_dev;
create user 'petstore'@'localhost' identified by 'petstore';
grant all on petstore_dev.* to 'petstore'@'localhost';
create database petstore_test;
create user 'testbot'@'localhost' identified by 'petstore';
grant all on petstore_test.* to 'testbot'@'localhost';
```

## Building

You need to install [PhantomJS](http://phantomjs.org) to run the end-to-end tests (tested on 1.9.7). Make sure you modify the 
end-to-end tests [properties](https://github.com/testinfected/simple-petstore/blob/master/server/src/test/resources/test.properties) 
to indicate the path of the PhantomJS executable. 

To build and run all tests:

`./gradlew build`

## Migrating the database

To prepare your database:

`./gradlew db-migrate`

Use the [seeds](https://github.com/testinfected/simple-petstore/blob/master/server/src/main/scripts/seeds/items.sql) to populate your MySQL database with sample data:

`mysql -u petstore -p petstore_dev < ./server/src/main/scripts/seeds/items.sql`

## Running

To run the application:

`./gradlew run`

## IntelliJ

If you use IntelliJ, just import the gradle build.
