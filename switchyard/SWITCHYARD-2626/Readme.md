A reproducer for SWITCHYARD-2626 : JBoss7PackageScanClassResolver doesn't look into war archive
============

This example is invoked through a File gateway binding. 

Running the quickstart
======================


EAP
----------
1. Start EAP in standalone mode:

        ${AS}/bin/standalone.sh

2. Build and deploy the Quickstart :

        mvn install -Pdeploy

3. 
<br/>
```
        Copy src/test/resources/file.txt to /tmp/inbox2/file.txt  
```
<br/>
* (If on Windows, change the file binding in switchyard.xml to a Windows directory path)

4. Undeploy the quickstart:

        mvn clean -Pdeploy


Expected Output
===============
```
[FileProcessorBean] 1|Fruit Loops|3.99
2|Lucky Charms|4.99
3|Grape Nuts|2.33

Processed Message : 1|Fruit Loops|3.99
2|Lucky Charms|17
3|Cheerios|2.33
```


## Further Reading

1. [Camel Bindy](http://camel.apache.org/bindy.html)
