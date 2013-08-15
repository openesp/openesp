@ECHO OFF
REM  Licensed to the Apache Software Foundation (ASF) under one or more
REM  contributor license agreements.  See the NOTICE file distributed with
REM  this work for additional information regarding copyright ownership.
REM  The ASF licenses this file to You under the Apache License, Version 2.0
REM  (the "License"); you may not use this file except in compliance with
REM  the License.  You may obtain a copy of the License at
REM
REM      http://www.apache.org/licenses/LICENSE-2.0
REM
REM  Unless required by applicable law or agreed to in writing, software
REM  distributed under the License is distributed on an "AS IS" BASIS,
REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
REM  See the License for the specific language governing permissions and
REM  limitations under the License.

REM You can override pass the following parameters to this script:
REM 

set JVM=java

REM  Find location of this script

set SDIR=%~dp0
if "%SDIR:~-1%"=="\" set SDIR=%SDIR:~0,-1%

REM EXAMPLE "$openEspInstallationDir\cloud-scripts\zkcli.bat -cmd upconfig -zkhost ${zooKeeperHost}:${zooKeeperPort} -confdir ${openEspInstallationDir}\conf\solr\collection1\conf -confname conf1"
REM EXAMPLE "$openEspInstallationDir\cloud-scripts\zkcli.bat -cmd linkconfig -zkhost ${zooKeeperHost}:${zooKeeperPort} -collection collection1 -confname conf1"

IF EXIST "%SDIR%\..\tomcat\webapps\solr" GOTO RUN_TOOL (
  echo ERROR: Missing required classes
  echo Please start and stop OpenESP once and try again
  exit /b 2
)
     
:RUN_TOOL
"%JVM%" -Dlog4j.configuration=file:%SDIR%\..\conf\log4j.stderr.properties -classpath "%SDIR%\..\tomcat\webapps\solr\WEB-INF\lib\*;%SDIR%\..\tomcat\lib\logging\*" org.apache.solr.cloud.ZkCLI %*

