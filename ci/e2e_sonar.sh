#!/bin/sh
if [ -z "$1" ]
  then
    VER="9.0.1.46107"
  else
    VER="$1"
fi
./get_sonar.sh "$VER"
./configure_sonar.sh "$VER"
"./sonarqube-$VER/bin/linux-x86-64/sonar.sh" console