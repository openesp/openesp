#!/bin/sh

pushd $(dirname "${0}") > /dev/null
BASEDIR=$(pwd -L)
popd > /dev/null
OPENESP_HOME=$BASEDIR/..
echo "Disabling MCF"
rm $OPENESP_HOME/tomcat/conf/Catalina/localhost/mcf.xml