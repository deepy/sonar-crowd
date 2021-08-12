## Quick and dirty system test environment

* `get_sonar.sh` downloads and unpacks sonarqube
* `configure_sonar.sh` copies the jar and installs the plugin
* `e2e_sonar.sh` runs both the above scripts and then starts sonar in console mode
* `run.sh` to be run from the host to start a test

```shell
 # Create a network called sonar-test, to allow both containers to speak with
 # each other, also name the sonar one: sonar 
docker network create sonar-test
docker run -v docker-crowd-home:/var/atlassian/application-data/crowd -p 8095:8095 --name sonar --network sonar-test atlassian/crowd
 # Start a container, last argument is version to test against 
docker run -it --rm -v "$(pwd)/target":/sonar-plugin:cached,ro -p 9000:9000 --network=sonar-test sonartest 8.9.0.43852
```

Create the following in Crowd:

| Tables            | name       | password |
| ----------------- | ---------- | -------- |
| Crowd User        | crowdadmin | admin    |
| Crowd User        | crowduser  | user     |
| Crowd Application | sonar      | sonar    |

And create a `sonar-administrators` group in Crowd with the `crowdadmin` as member

Also make sure to add `172.*.*.*` (or similar) to your remote hosts in the Crowd application