#!/bin/bash
KANELA_JAR_PATH=~/Downloads
export JAVA_HOME=~/.cache/coursier/jvm/adopt@1.11.0-11
export PATH=${JAVA_HOME}/bin:$PATH
sed -i 's|javaOptions++=Seq()|javaOptions++=Seq("-Xms3096M","-Xmx3096M","-XX:+UnlockExperimentalVMOptions","-XX:+UseShenandoahGC")|g' build.sbt

sbt run -J-javaagent:$KANELA_JAR_PATH/kanela-agent-1.0.13.jar
