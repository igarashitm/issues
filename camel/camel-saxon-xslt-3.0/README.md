Camel Saxon XSLT 3.0
=================

Apache Karaf
-------------------------
You will need to compile this example first:
    mvn clean install

To install Apache Camel in Karaf you type in the shell

    features:addurl mvn:org.apache.camel.karaf/apache-camel/2.16-SNAPSHOT/xml/features

First you need to install the following features in Karaf/ServiceMix with:

    features:install camel-blueprint
    features:install camel-saxon

Then you can install the Camel example:

    osgi:install -s mvn:org.apache.camel/camel-saxon-xslt-3.0/2.16-SNAPSHOT


