#!/usr/bin/env bash

#  Licensed to the Apache Software Foundation (ASF) under one or more
#  contributor license agreements.  See the NOTICE file distributed with
#  this work for additional information regarding copyright ownership.
#  The ASF licenses this file to You under the Apache License, Version 2.0
#  (the "License"); you may not use this file except in compliance with
#  the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

# You can override pass the following parameters to this script:
# 

JVM="java"

# Find location of this script

sdir="`dirname \"$0\"`"

if [ ! -e $sdir/../tomcat/webapps/solr ] ; then
  echo "ERROR: Missing required classes."
  echo "Please start and stop OpenESP once and try again"
  exit 2
fi

PATH=$JAVA_HOME/bin:$PATH $JVM -Dlog4j.configuration=file:$sdir/../conf/log4j.stderr.properties -classpath "$sdir/../tomcat/webapps/solr/WEB-INF/lib/*:$sdir/../tomcat/lib/logging/*" org.apache.solr.cloud.ZkCLI ${1+"$@"}

