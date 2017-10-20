# Persephone [![Build Status](https://api.travis-ci.org/vianneyfaivre/Persephone.svg?branch=master)](https://travis-ci.org/vianneyfaivre/Persephone)

Web application that ease the monitoring of Spring boot applications. 

Built with Vaadin and Spring Boot.

## Features

For each monitored application:
* Display a lot of metrics
* List all properties
* Tail logs
* Download full logs file
* View / update loggers configuration
* List the last 100 HTTP requests

## Pre-requisites : Applications to monitor

Please note that only Spring Boot versions 1.3.x, 1.4.x and 1.5.x are supported.

**1. Spring Boot actuator enabled**

Add the following dependency to your POM:

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**2. Configure security for Actuator endpoints**

* Enable: [See Spring documentation](https://docs.spring.io/spring-boot/docs/1.5.7.RELEASE/reference/htmlsingle/#production-ready-sensitive-endpoints)
* Disable: set the property `endpoints.sensitive` to `false` (**don't do it in production!**).

**3. Set property `logging.path` or `logging.file`**

This will enable the use of Actuator endpoint GET /logfile (available since Spring Boot 1.5)

## Pre-requisites : Persephone

Applications that you want to monitor have to be listed in a CSV file.

**1. CSV format**

```
Application Name,Environment,URL,Actuator Auth Scheme,Actuator username,Actuator password
Persephone,dev,http://localhost:9191,BASIC,admin,admin
Persephone,dev-secured,http://devmachine:9191,,,
```

**2. Path to the CSV**

Set the property `persephone.applications.csv` with the path to the CSV file. 

## Getting started

On Windows:

`mvnw.cmd spring-boot:run -Dpersephone.applications.csv=C:/path/to/applications.csv`

On Unix/Linux:

`mvnw spring-boot:run -Dpersephone.applications.csv=/path/to/applications.csv`
