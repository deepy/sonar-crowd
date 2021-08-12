#!/bin/sh
if [ -z "$1" ]
  then
    VER="9.0.1.46107"
  else
    VER="$1"
fi
wget "https://binaries.sonarsource.com/Distribution/sonarqube/sonarqube-$VER.zip"
unzip "sonarqube-$VER.zip"