#!/bin/bash
N=${1}
echo "Populating data $N"
ab -p scripts/alarm_payload.json -T application/json -c 4 -n $N http://localhost:9290/alarms

