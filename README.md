```
"The earth has music for
those who listen."

– William Shakespeare
```

<div align="center">
    <img src="https://media.giphy.com/media/XAaRqeePpXw6Jy9Irs/giphy.gif" alt="Found @ Giphy by @todprod" width="200">
    <br>
    <h1>Monitoring Schule</h1>
    <sub>Built with ❤︎ </sub>
</div>


# monitoring_schule
##Preconditions: 

<b>Install [docker](https://docs.docker.com/engine/install/) and [docker-compose](https://docs.docker.com/compose/install/)</b>

<b>Install [apache bench](https://httpd.apache.org/docs/2.4/programs/ab.html)</b>

<b>Download [Kanela Agent jar](https://mvnrepository.com/artifact/io.kamon/kanela-agent)</b>


## Objective
Experiment with monitoring using KGPZ (Kamon Grafana Prometheus Zipkin) 

### Run
Use Kanela Agent in order to run with Kamon Instrumentation
-javaagent:/${KANELA_JAR_PATH}/kanela-agent-1.0.7.jar

e.g.:-javaagent:/home/smagnacco/Downloads/kanela-agent-1.0.7.jar

###URLS
GET http://localhost:9290/alarms

GET http://localhost:9290/alarms?id=someId

POST http://localhost:9290/alarms entity={'id':'someId'}

DELETE http://localhost:9290/alarms?id=someId

### Stress test
post file using:
```bash
ab -p scripts/alarm.json -T application/json -c 4 -n 100000000 http://localhost:9290/alarms
```

get:
```bash
ab -p scripts/alarm.json -T application/json -c 4 -n 100000000 http://localhost:9290/alarms
```

## Monitoring

#### Kamon
http://0.0.0.0:5266/

#### Prometheus
App data http://0.0.0.0:9095

Prometheus Graph http://0.0.0.0:9090/graph

#### GRAFANA
http://localhost:3000/login

Use import dashboard for:
Kamon JVM id: 8808


#### ZIPKING     
http://localhost:9411/zipkin/


## Directory Orgaqnization
### Docker directory
The docker directory contains grafana, prometheus and zipking configuration out of the box. Those will work in memory, no data will be stored, there is a fake store e.g. Inside grafana directory, you will find dashboards directory, that's the place you will keep a copy of your finished dashboards in order to track dashboard changes. 

### Scripts
Scripts will contain testing utilities
