#!/usr/bin/env bash

##########################################################
# This is a DRAFT start script for development use only.
# For production use, please start OpenESP as a service
##########################################################

pushd $(dirname "${0}") > /dev/null
BASEDIR=$(pwd -L)
popd > /dev/null
OPENESP_HOME=$BASEDIR/..
echo "Starting OpenESP (OPENESP_HOME=$OPENESP_HOME)"
SOLR_HOME=$OPENESP_HOME/conf/solr
SOLR_DATA_DIR=$OPENESP_HOME/data/solr
SOLR_LOG_DIR=$OPENESP_HOME/logs/solr
export CATALINA_OPTS="-server \
                     -verbose:gc \
                     -XX:+UseConcMarkSweepGC \
                     -XX:+CMSClassUnloadingEnabled \
                     -XX:-CMSParallelRemarkEnabled \
                     -XX:+UseCMSCompactAtFullCollection \
                     -XX:+UseParNewGC \
                     -XX:+PrintGCDetails \
                     -Xms512m -Xmx1024m \
                     -Dsolr.solr.home=$SOLR_HOME \
                     -Dopenesp.home=$OPENESP_HOME \
                     -Dsolr.data.dir=$SOLR_DATA_DIR \
                     -Dsolr.log.dir=$SOLR_LOG_DIR \
                     -Dlog4j.debug=true \
                     -Dlog4j.configuration=file:///$OPENESP_HOME/conf/solr/log4j.properties \
                     -Dcom.sun.management.jmxremote \
                     -Dcom.sun.management.jmxremote.port=18086 \
                     -Dcom.sun.management.jmxremote.ssl=false \
                     -Dcom.sun.management.jmxremote.authenticate=false \
                     -Dorg.apache.manifoldcf.configfile=$OPENESP_HOME/conf/mcf/properties.xml"
echo CATALINA_OPTS is $CATALINA_OPTS

pushd $OPENESP_HOME/tomcat/bin
sh ./catalina.sh run
popd