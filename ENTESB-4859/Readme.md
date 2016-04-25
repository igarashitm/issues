An example to apply CXF configuration for SOAP reference binding
============



#### test case
Use cxf.config.file system property
```
mvn test -Dcxf.config.file=src/main/resources/cxf.xml
```


#### EAP
Use [jboss-deployment-structure.xml](src/test/resources/META-INF/jboss-deployment-structure.xml)
```
mvn -Pdeploy install
```
test
```
cp src/test/resources/test.txt $EAP_HOME/target/input
```



#### karaf
Use [blueprint.xml](src/test/resources/OSGI-INF/blueprint/blueprint.xml)
```
karaf@root> features:addurl mvn:org.switchyard.karaf/switchyard/2.1.0-SNAPSHOT/xml/features
karaf@root> features:addurl mvn:com.github.igarashitm.issues/ENTESB-4859/2.1.0-SNAPSHOT/xml/features
karaf@root> features:install ENTESB-4859 
karaf@root>
```
test
```
$ cp src/test/resources/test.txt $KARAF_HOME/target/input
```



#### Expected Output
```
Expected fault was received, 'Whoa!'
```
