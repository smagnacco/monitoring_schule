#!/bin/bash
KANELA_JAR_PATH=~/Downloads

export JAVA_HOME=~/.cache/coursier/jvm/adopt@1.8.0-292
export PATH=${JAVA_HOME}/bin:$PATH

sed -i 's|javaOptions++=Seq("-Xms3096M","-Xmx3096M","-XX:+UnlockExperimentalVMOptions","-XX:+UseZGC")|javaOptions++=Seq()|g' build.sbt
sbt run -J-javaagent:$KANELA_JAR_PATH/kanela-agent-1.0.13.jar

