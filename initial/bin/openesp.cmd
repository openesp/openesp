@echo off
REM *********************************************************
REM * This is a DRAFT start script for development use only.
REM * For production use, please start OpenESP as a service
REM *********************************************************

echo "Starting OpenESP"

SET OPENESP_HOME=%cd%\..
SET SOLR_HOME=%OPENESP_HOME%\conf\solr
SET SOLR_DATA_DIR=%OPENESP_HOME%/data/solr
SET CATALINA_OPTS=-Xms512m -Xmx1024m -Dsolr.solr.home=%SOLR_HOME% -Dopenesp.home=%OPENESP_HOME% -Dsolr.data.dir=%SOLR_DATA_DIR%
echo CATALINA_OPTS is %CATALINA_OPTS%

cd %OPENESP%\tomcat\bin
catalina.bat run
