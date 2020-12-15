#!/bin/bash
echo 'Generating traffic'
C=$1
N=$2

ab -T application/json -c $C -n $N http://localhost:9290/alarms/1
