mvn -Pdeploy install
restart EAP (applying shorter transaction timeout which requires restart)
touch $EAP_HOME/target/input/input.txt
wait 30secs
