[![Build Status](https://travis-ci.org/testinfected/simple-petstore.png)](https://travis-ci.org/testinfected/simple-petstore)
 
# About

A fork of my [petstore](https://github.com/testinfected/petstore) repository, but re-built with simple tools and no IoC container.

It uses the following tools:

- [Simpleframework](http://www.simpleframework.org/) to replace Spring MVC
- [mustache](http://mustache.github.com) for logic-less templating
- Plain JDBC to replace Hibernate
- No IoC container instead of Spring
- [Buildr](http://buildr.apache.org) to replace Maven

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

To build the application, I use rvm with ruby-1.8.7-p334 and Buildr 1.4.12. Build on Travis runs on both ruby-1.9.3 and JRuby (1.9) with latest Buildr (1.4.12 at the time of writing).

Buildr 1.4.8+ supports Java 7.

To install Buildr, follow Buildr installation [instructions](http://buildr.apache.org/installing.html).

To build and run all tests, use:

`buildr install`

You need to have either PhantomJS or Firefox installed to run the end-to-end tests.

Modify the [test configuration file](https://github.com/testinfected/simple-petstore/blob/master/server/src/test/resources/test.properties) according to your settings. Note that you have to specify the path of the phantomjs executable.

## Migrating the database

To migrate your database, from the top-level directory use:

`buildr db-migrate`

Use the [seeds](https://github.com/testinfected/simple-petstore/blob/master/server/src/main/scripts/seeds/items.sql) to populate your MySQL database with sample data.

## Running

To run the application, from the top-level directory use:

`buildr run`

## IntelliJ

If you use IntelliJ, you can generate your project using:

`buildr idea`