start EAP with standalone-full.xml
mvn -Pdeploy install
restart EAP
touch $EAP_HOME/target/input/input.txt
wait 30secs
