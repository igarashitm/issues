CAUTION
=======

This is an antipattern. We strongly recommend to bundle Bean implementation
into SwitchYard application itself, but not expose as AS module.

See this community forum for more details:
https://developer.jboss.org/thread/249134


Instructions
============

	mvn clean install
	mkdir -p ${EAP_HOME}/modules/system/layers/soa/switchyard-bean-module-service/main
	cp bean-module-service/module.xml ${EAP_HOME}/modules/system/layers/soa/switchyard-bean-module-service/main/
	cp bean-module-service/target/switchyard-bean-module-service.jar ${EAP_HOME}/modules/system/layers/soa/switchyard-bean-module-service/main/
	cp bean-module-client/target/switchyard-bean-module-client.jar ${EAP_HOME}/standalone/deployments/
	cd bean-module-client
	mvn exec:java
