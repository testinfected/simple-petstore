# About

A fork of my [petstore](https://github.com/testinfected/petstore) repository, but built with simple tools and no IoC container.

## Disclaimer

This is a work in progress.

The intention is to progressively refactor the petstore to get rid of Spring Core, Spring MVC, Hibernate and Maven.

I have in mind to use the following tools:

- [Simpleframework](http://www.simpleframework.org/) to replace Spring MVC
- Velocity and Sitemesh for the view (like in my original petstore)
- Plain JDBC to replace Hibernate
- No IoC container to replace Spring Core
- [Buildr](http://buildr.apache.org) to replace Maven