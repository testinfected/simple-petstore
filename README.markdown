[![Build Status](https://travis-ci.org/testinfected/simple-petstore.png)](https://travis-ci.org/testinfected/simple-petstore)

# About

A fork of my [petstore](https://github.com/testinfected/petstore) repository, but re-built with simple tools and no IoC container.

It uses the following tools:

- [Simple](http://www.simpleframework.org/), an embeddable high-performance HTTP server
- [Mustache](http://mustache.github.com) for logic-less templating
- Plain JDBC
- No IoC container
- [Buildr](http://buildr.apache.org) or [Gradle](http://www.gradle.org) for the build system

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

You need to have either PhantomJS or Firefox installed (tested on Firefox 24) to run the end-to-end tests.

Modify the [test configuration file](https://github.com/testinfected/simple-petstore/blob/master/server/src/test/resources/test.properties) according to your settings. Note that you have to specify the path of the phantomjs executable.

To build and run all tests:

`buildr package`

or

`gradle build`

## Migrating the database

To prepare your database:

`buildr db-migrate`

or

`gradle db-migrate`

Use the [seeds](https://github.com/testinfected/simple-petstore/blob/master/server/src/main/scripts/seeds/items.sql) to populate your MySQL database with sample data:

`mysql -u petstore -p petstore_dev < path/to/items.sql`

## Running

To run the application:

`buildr run`

or

`gradle run`

## IntelliJ

If you use IntelliJ, you can generate your project using:

`buildr idea`