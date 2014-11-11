#!/bin/sh

BM_OPTIONS="-Dorg.jboss.byteman.transform.all"
BM_OPTIONS="$OPTIONS -Dorg.jboss.byteman.verbose"
BM_OPTIONS="$OPTIONS -b"
BM_SCRIPTS="byteman-rules.btm"

$BYTEMAN_HOME/bin/bmcheck.sh $BM_SCRIPTS
$BYTEMAN_HOME/bin/bminstall.sh $BM_OPTIONS /home/JBossEAP/SY-2.0/jboss-modules.jar
$BYTEMAN_HOME/bin/bmsubmit.sh $BM_SCRIPTS
