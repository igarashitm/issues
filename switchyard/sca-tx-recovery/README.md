A few patterns of XA recovery scenario using SCA binding
================================================

Patterns
-----------
* __jpa-local-sca-jms__ : {DealerService->JPA} ---> SCA local invocation ---> {CreditService->JMS}
* __jpa-local-sca-jms-jpa__ : {DealerService->JPA} ---> SCA local invocation ---> {CreditService->{JMS,JPA}}
* __jpa-remote-sca-jms__ : {DealerService->JPA} ---> SCA remote invocation ---> {CreditService->JMS}
* __jpa-remote-sca-jms-jpa__ : {DealerService->JPA} ---> SCA remote invocation ---> {CreditService->{JMS,JPA}}


Instruction
-----------
1. Install PostgreSQL driver as a JBoss module (use modules-org.postgresql.jdbc)

1. Start JBoss EAP instance

1. Build and deploy the application onto JBoss EAP, and run config.cli to setup resources

         mvn -Pdeploy install

1. Restart JBoss EAP instance

1. Setup byteman (bminstall & bmsubmit)

        sh bmsetup.sh

1. Execute test client

        mvn exec:java

1. Start JBoss EAP instance again and wait for the periodic recovery to replay phase2 commit

1. Undeploy and cleanup with unconfig.cli

        mvn -Pdeploy clean

