jpa-binding
============

This application demonstrates Transaction Policy management on multiple camel-jpa bindings those are bound to the same H2 database.

Test instructions
============

1. Start the AS7 with standalone-full.xml profile:
````
    sh ${AS7}/bin/standalone.sh -c standalone-full.xml
````

3. Execute the testcase
````
    mvn test -DskipTests=false
````
