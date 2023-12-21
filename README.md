# What is this project?

It's basically a Kotlin version of [Consuming a SOAP web service](https://spring.io/guides/gs/consuming-web-service/).

Only http://localhost:8082/api/v1/countries works.

## Prerequisite

A SOAP producer is needed to run this demo app. The easiest way is to use Docker:

```
$ docker run --rm -d -p 18081:8080 --name my-soap-producer mahata/soap-demo-producer:latest
```

The SOAP Producer is based on [this tutorial](https://spring.io/guides/gs/producing-web-service/). 
