#!/bin/sh
if [ -z "$1" ]
  then
    VER="9.0.1.46107"
  else
    VER="$1"
fi
cat << EOF > "/work/sonarqube-$VER/conf/sonar.properties"
sonar.telemetry.enable=false
sonar.security.realm=Crowd
crowd.url=http://sonar:8095/crowd/
crowd.application=sonar
crowd.password=sonar
crowd.groups.sync=true
sonar.security.localUsers=admin,sonar
EOF
cp /sonar-plugin/sonar-crowd-plugin-*.jar "/work/sonarqube-$VER/extensions/plugins/"