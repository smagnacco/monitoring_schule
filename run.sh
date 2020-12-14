#!/bin/bash
KANELA_JAR_PATH=/home/smagnacco/Downloads
sbt run -J-javaagent:$KANELA_JAR_PATH/kanela-agent-1.0.7.jar

