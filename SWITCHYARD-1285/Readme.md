SWITCHYARD-1285
============
https://issues.jboss.org/browse/SWITCHYARD-1285

This is used to be a reproducer for the SWITCHYARD-1285: Transaction Policy doesn't work with camel-jms, but it turned out the problem can be avoided if distinct pooled-connection-factory or xa-datasource is configured for each binding. So I put those examples here.

jms-binding
============
If the transaction boundary is needed to be put between the interactions with the same JMS message broker, distinct managed connection factory should be configured for each binding if camel-jms is used. This example application demonstrates Transaction Policy management on camel-jms binding with declaring 2 more distinct HornetQ pooled-connection-factory "JmsXA2" and "JmsXA3" so 3 of each camel-jms binding could have its dedicated one. If you just use "JmsXA" for all of camel-jms binding on this application, the Transaction Policy doesn't work as expected.

jpa-binding
============
Same thing for camel-jpa binding - in order to get Transaction Policy working on camel-jpa, 2 distinct xa-datasource "GreetXADS" and "Greet2XADS" is configured so 2 of each camel-jpa binding could have its dedicated datasource.

sql-binding
============
Same as jpa-binding, but using camel-sql binding.
