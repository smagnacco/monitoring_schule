#!/bin/bash
./vegetaSpitter.py | vegeta attack -duration=10s | tee result.bin | vegeta report
