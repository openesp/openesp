@echo off
REM *********************************************************
REM * This is a DRAFT start script for development use only.
REM * For production use, please start OpenESP as a service
REM *********************************************************

echo "Detecting JAVA_HOME"
if "%JAVA_HOME%"=="" call:FIND_JAVA_HOME
echo "Java home: %JAVA_HOME%"
rem Start your java application or perform other actions here
goto:END

:FIND_JAVA_HOME
FOR /F "skip=2 tokens=2*" %%A IN ('REG QUERY "HKLM\Software\JavaSoft\Java Runtime Environment" /v CurrentVersion') DO set CurVer=%%B
FOR /F "skip=2 tokens=2*" %%A IN ('REG QUERY "HKLM\Software\JavaSoft\Java Runtime Environment\%CurVer%" /v JavaHome') DO set JAVA_HOME=%%B
goto:EOF
:END

echo "Starting OpenESP"
SET OPENESP_HOME=%~dp0..
SET SOLR_HOME=%OPENESP_HOME%\conf\solr
SET SOLR_DATA_DIR=%OPENESP_HOME%/data/solr
SET CATALINA_OPTS=-Xms1024m -Xmx1024m -Dsolr.solr.home="%SOLR_HOME%" -Dopenesp.home="%OPENESP_HOME%" -Dsolr.data.dir="%SOLR_DATA_DIR%" -Dorg.apache.manifoldcf.configfile="%OPENESP_HOME%\conf\mcf\properties.xml" -Dlog4j.configuration="file:///%OPENESP_HOME%\conf\solr\log4j.properties" -Dlog4j.debug=true -Dsolr.log.dir="%OPENESP_HOME%\logs\solr -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=18086 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
echo CATALINA_OPTS is %CATALINA_OPTS%

cd "%OPENESP_HOME%\tomcat\bin"
catalina.bat run
