# monitoring_schule
##Preconditions: 
<b>Install docker and docker-compose</b>
<b>Install apache bench</b>
<b>Download Kanela Agent jar</b>

## Objective
Simple http server for alarm toy project in order to play with monitoring

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