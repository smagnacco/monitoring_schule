#!/bin/bash
KANELA_JAR_PATH=/home/smagnacco/Downloads
export JAVA_HOME=/home/smagnacco/programs/langs/jdk-11.0.7+10
export PATH=${JAVA_HOME}/bin:$PATH

sed -i 's|javaOptions++=Seq("-Xms3096M","-Xmx3096M","-XX:+UnlockExperimentalVMOptions","-XX:+UseZGC")|javaOptions++=Seq()|g' build.sbt
sbt run -J-javaagent:$KANELA_JAR_PATH/kanela-agent-1.0.11.jar

