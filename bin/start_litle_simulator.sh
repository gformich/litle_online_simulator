#!/bin/sh

# setup classpath
CLASSPATH=../lib/${CLASSPATH};

java -Dlitle.simulator.port=8080 -jar SimpleLitleOnlineSimulator.jar