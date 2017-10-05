# Persephone

Web application that ease the monitoring of Spring boot applications.

## Features

For each monitored application:
* Display some metrics
* List all application properties
* Show / download real time logs
* View / update loggers configuration

## Pre-requisites : Applications to monitor

**Spring Boot actuator enabled**

Add the following dependency to your POM:

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**Disable security for Actuator endpoints**

Set the property `endpoints.sensitive` to `false`.

**Property `logging.path` or `logging.file` set**

This will enable the use of Actuator endpoint GET /logfile.

## Pre-requisites : Persephone

Applications that you want to monitor have to be listed in a CSV file.

**CSV format**

```
Application Name,Environment,URL
Persephone,local,http://localhost:9191
Persephone,dev,http://devmachine:9191
```

**Path to the CSV**

Set the property `persephone.applications.csv` with the path to the CSV file. 

## Getting started

On Windows:

`mvnw.cmd spring-boot:run -Dpersephone.applications.csv=C:/path/to/applications.csv`

On Unix/Linux:

`mvnw spring-boot:run -Dpersephone.applications.csv=/path/to/applications.csv`
