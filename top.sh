#!/bin/bash

top |  grep `jps | grep MonitorAlarmApp | cut -d ' ' -f 1`
