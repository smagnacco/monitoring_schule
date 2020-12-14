#!/bin/bash
echo 'Generating traffic'

ab -T application/json -c 8 -n 100000000 http://localhost:9290/alarms/1
