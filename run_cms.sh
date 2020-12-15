#!/bin/bash
KANELA_JAR_PATH=/home/smagnacco/Downloads

sed -i 's|javaOptions++=Seq("-Xms3096M","-Xmx3096M","-XX:+UnlockExperimentalVMOptions","-XX:+UseZGC")|javaOptions++=Seq()|g' build.sbt
sbt run -J-javaagent:$KANELA_JAR_PATH/kanela-agent-1.0.7.jar

