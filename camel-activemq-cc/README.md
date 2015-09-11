Camel ActiveMQ concurrentConsumers setting
=================

Apache Karaf
-------------------------
You will need to compile this example first:
    mvn clean install

To install Apache Camel in Karaf you type in the shell

    features:addurl mvn:org.apache.camel.karaf/apache-camel/2.15.1/xml/features
    features:addurl mvn:org.apache.activemq/activemq-karaf/5.12.0/xml/features

First you need to install the following features in Karaf/ServiceMix with:

    features:install camel-blueprint
    features:install activemq-broker
    features:install activemq-camel

Then you can install the Camel example:

    osgi:install -s mvn:org.apache.camel/camel-saxon-xslt-3.0/2.15.1


