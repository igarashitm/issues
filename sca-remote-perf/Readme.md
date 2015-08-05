# A simple performance test for SCA remote invocation

*1. Start EAP instances
````
    ./bin/standalone.sh -c standalone-ha.xml
    ./bin/standalone.sh -Djboss.node.name=node2 -Djboss.server.base.dir=${EAP_HOME}/node2 -Djboss.socket.binding.port-offset=1000 -c standalone-ha.xml
````

*2. Run a test
````
    mvn clean test
````

*3. Check the result
````
    >>>>>>>>>>>>>>> testSharedInvoker >>>>>>>>>>>>>>>>
    >>>>> Result : 481[tps]
    >>>>> Duration=103824[ms], Concurrency=100, Repeat=500
    >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    >>>>>>>>>>>>>>> test >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    >>>>> Result : 159[tps]
    >>>>> Duration=313735[ms], Concurrency=100, Repeat=500
    >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
````
