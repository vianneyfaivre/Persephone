# Persephone

Web application that ease the monitoring of Spring boot applications.

## Features

For each monitored application:
* Display some metrics
* List all application properties
* Show / download real time logs

## Pre-requisites

**Applications to monitor**

* Spring Boot actuator enabled 
* Property "logging.path" or "logging.file" set

**List of applications**

Applications that you want to monitor have to be listed in a CSV file.

* CSV format

```
Application Name,Environment,URL
Persephone,local,http://localhost:9191
Persephone,dev,http://devmachine:9191
```

* Path to the CSV

Set the property 'persephone.applications.csv' with the path to the CSV file. 

## Run

`mvn spring-boot:run -Dpersephone.applications.csv=/path/to/applications.csv`