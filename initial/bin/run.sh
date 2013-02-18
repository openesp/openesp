#!/bin/sh

##########################################################
# This is a DRAFT start script for development use only.
# For production use, please start OpenESP as a service
##########################################################

echo "Starting OpenESP"

OPENESP_HOME=`pwd`/..
SOLR_HOME=$OPENESP_HOME/conf/solr
SOLR_DATA_DIR=$OPENESP_HOME/data/solr
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
                     -Dorg.apache.manifoldcf.configfile=$OPENESP_HOME/conf/mcf/properties.xml \
                     -Djava.util.logging.config.file=$OPENESP_HOME/conf/solr/logging.properties"
echo CATALINA_OPTS is $CATALINA_OPTS

# -Djava.util.logging.config.file=$OPENESP_HOME/conf/solr/logging.properties

pushd $OPENESP_HOME/tomcat/bin
sh ./catalina.sh run
popd