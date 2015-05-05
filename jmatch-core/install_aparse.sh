#!/bin/bash

VERSION=2.5
DOWNLOAD_DIR=~/downloads

pushd $DOWNLOAD_DIR

if ! [ -f aparse-$VERSION.jar ]; then
	curl -L -H 'Referer: http://www.parse2.com/download.shtml' -O http://www.parse2.com/downloads/aparse-$VERSION.jar 
fi

if ! [ -f aparse-maven-plugin-$VERSION.jar ]; then
	curl -L -H 'Referer: http://www.parse2.com/download.shtml' -O http://www.parse2.com/downloads/aparse-maven-plugin-$VERSION.jar
fi

mvn install:install-file -Dfile=aparse-$VERSION.jar -DgroupId=com.parse2.aparse \
    -DartifactId=aparse -Dversion=$VERSION -Dpackaging=jar

mvn install:install-file -Dfile=aparse-maven-plugin-$VERSION.jar -DgroupId=com.parse2.aparse \
    -DartifactId=aparse-maven-plugin -Dversion=$VERSION -Dpackaging=jar

popd
