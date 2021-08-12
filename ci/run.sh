#!/bin/sh
if [ -z "$1" ]
  then
    VER="9.0.1.46107"
  else
    VER="$1"
fi
docker run -it --rm -v "$(pwd)/target":/sonar-plugin:cached,ro -p 9000:9000 --network=sonar-test sonartest "$VER"
mvn verify -D skip.failsafe.tests=false