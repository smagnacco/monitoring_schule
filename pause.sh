#!/bin/bash
PID=`jps | grep MonitorAlarmApp | cut -d ' ' -f 1`
echo "Simulate a pause for $PID"
jmap -histo $PID > /dev/null
