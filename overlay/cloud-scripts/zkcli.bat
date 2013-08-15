REM You can override pass the following parameters to this script:
REM 

set JVM=java

REM  Find location of this script

set SDIR=%~dp0
if "%SDIR:~-1%"=="\" set SDIR=%SDIR:~0,-1%

REM EXAMPLE "$openEspInstallationDir\cloud-scripts\zkcli.bat -cmd upconfig -zkhost ${zooKeeperHost}:${zooKeeperPort} -confdir ${openEspInstallationDir}\conf\solr\collection1\conf -confname conf1"
REM EXAMPLE "$openEspInstallationDir\cloud-scripts\zkcli.bat -cmd linkconfig -zkhost ${zooKeeperHost}:${zooKeeperPort} -collection collection1 -confname conf1"
     
"%JVM%" -classpath "%SDIR%\..\tomcat\webapps\solr\WEB-INF\lib\*;%SDIR%\..\tomcat\lib\logging\*" org.apache.solr.cloud.ZkCLI %*
