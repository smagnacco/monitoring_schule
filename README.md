# monitoring_schule
##Preconditions: 
<b>Install docker and docker-compose</B>
<b>Install apache bench</B>

## Objective
Simple http server for alarm toy project in order to play with monitoring

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