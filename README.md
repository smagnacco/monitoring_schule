```
"The earth has music for
those who listen."

– William Shakespeare
```

<div align="center">
    <img src="https://media.giphy.com/media/l1Uq4o4v6K3HShcIBy/giphy.gif" alt="Found @ Giphy by @tonybabel" width="200">
    <br>
    <h1>Monitoring Schule</h1>
    <sub>Built with ❤︎ </sub>
</div>


## Preconditions: 

<b>Install [docker](https://docs.docker.com/engine/install/) and [docker-compose](https://docs.docker.com/compose/install/)</b>

<b>Install [apache bench](https://httpd.apache.org/docs/2.4/programs/ab.html)</b>

<b>Download [Kanela Agent jar](https://mvnrepository.com/artifact/io.kamon/kanela-agent)</b>


## Objective
Experiment with monitoring using KGPZ (Kamon Grafana Prometheus Zipkin) 

## First start docker
```bash 
cd docker; ./startup; cd ..
```

### Run using SBT
Use Kanela Agent in order to run with Kamon Instrumentation

```-javaagent:/${KANELA_JAR_PATH}/kanela-agent-1.0.7.jar```

```bash
sbt run -J-javaagent:/${KANELA_JAR_PATH}/kanela-agent-1.0.7.jar
```
```bash
sbt run -J-javaagent:/home/smagnacco/Downloads/kanela-agent-1.0.7.jar
```

### URLS
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
ab -T application/json -c 4 -n 100000000 http://localhost:9290/alarms
```

## Monitoring

#### Kamon
http://0.0.0.0:5266/

#### Prometheus
App data http://0.0.0.0:9095

Prometheus Graph http://0.0.0.0:9090/graph

#### GRAFANA
http://localhost:3000/login


#### ZIPKING     
http://localhost:9411/zipkin/


## Directory Orgaqnization
### Docker directory
The docker directory contains grafana, prometheus and zipking configuration out of the box. Those will work in memory, no data will be stored, there is a fake store e.g. Inside grafana directory, you will find dashboards directory, that's the place you will keep a copy of your finished dashboards in order to track dashboard changes. 

### Scripts
Scripts will contain testing utilities

### Monitoring
jconsole pid will show you in real time cpu and mem of the pid process. With jconsole you can generate a full GC and watch a pause in your app in realtime.

Another way to watch heap size is using jmap, however, jmap -histo causes a full GC in order to collect information.

```jmap -histo pid | head 40```

e.g```jmap -histo:live `jps | grep MonitorAlarmApp | cut -d " " -f 1` | head```

If you need to monitor which threads are running

```jstack pid```
```jstack `jps | grep MonitorAlarmApp | cut -d " " -f 1` ```

## Utilities
```htop -p pid ``` where pid is your main java app, will help you to filter mem, cpu, for the given process (without noise of other process running in the system)
```vmstat 1 s``` r = running queue, pay attention to this number, those are tasks executing and waiting for CPU. If exceeds the number of CPUs on the server you have a bottleneck. [More Info](http://www.dba-oracle.com/t_tuning_vmstat.htm)
```bash
procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
 r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
 4  0  12032 536184 340180 4577316    0    0     0     0 13497 217487 55 20 25  0  0
 6  0  12032 533880 340188 4576128    0    0     0   188 13245 275146 54 18 28  0  0
 8  0  12032 533080 340212 4574920    0    0     0   136 12666 250987 56 17 27  0  0
 3  0  12032 531528 340212 4575276    0    0     0  1132 13172 211505 57 18 24  0  0
 5  0  12032 529356 340212 4574264    0    0     0    12 13960 163596 62 18 20  0  0
 3  0  12032 526016 340212 4575392    0    0     0     0 14159 206802 55 20 25  0  0
 7  0  12032 523132 340212 4575376    0    0     0    12 13394 174712 59 20 21  0  0
 4  0  12032 521672 340236 4574884    0    0     0   140 14588 198946 56 20 24  0  0
11  0  12032 514564 340252 4575612    0    0     0  8964 13985 170783 60 18 22  0  0
```

```bash
top | grep `jps | grep MonitorAlarmApp | cut -d ' ' -f 1` ```
