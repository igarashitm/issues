jms-binding
============

This application demonstrates Transaction Policy management on multiple camel-jms bindings those are bound to the same HornetQ server.

Test instructions
============

1. Add following pooled-connection-factory definitions to the hornetq configuration in the AS7 standalone-full.xml:
````xml
    <pooled-connection-factory name="hornetq-ra2">
        <transaction mode="xa"/>
        <connectors>
            <connector-ref connector-name="in-vm"/>
        </connectors>
        <entries>
            <entry name="java:/JmsXA2"/>
        </entries>
    </pooled-connection-factory>
    <pooled-connection-factory name="hornetq-ra3">
        <transaction mode="xa"/>
        <connectors>
            <connector-ref connector-name="in-vm"/>
        </connectors>
        <entries>
            <entry name="java:/JmsXA3"/>
        </entries>
    </pooled-connection-factory>
````

2. Start the AS7 with standalone-full.xml profile:
````
    sh ${AS7}/bin/standalone.sh -c standalone-full.xml
````

3. Execute the testcase
````
    mvn test -DskipTests=false
````
