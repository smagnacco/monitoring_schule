#!/bin/bash
PID=`jps | grep MonitorAlarmApp | cut -d ' ' -f 1`

jcmd $PID  VM.flags
