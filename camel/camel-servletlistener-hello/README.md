An example to run CamelContext within simple war
================================================

* Run a test (run with jetty)
```
mvn clean verify
```

* Run on a servlet container (tested with tomcat)
```
mvn clean package
cp target/camel-servletlistener-hello.war ${path.to.deploydir}
curl http://localhost:8080/camel-servletlistener-hello/
```

