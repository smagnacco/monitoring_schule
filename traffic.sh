#!/bin/bash
echo 'Generating traffic'

ab -T application/json -c 4 -n 100000000 http://localhost:9290/alarms
