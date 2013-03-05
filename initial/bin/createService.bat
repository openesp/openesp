set apath=%2
REM remove surrounding quotes
for /f "useback tokens=*" %%a in ('%apath%') do set apath=%%~a

set openesphome=%2
for /f "useback tokens=*" %%a in ('%openesphome%') do set openesphome=%%~a
set solrhome=%4
for /f "useback tokens=*" %%a in ('%solrhome%') do set solrhome=%%~a
set datadir=%5
for /f "useback tokens=*" %%a in ('%datadir%') do set datadir=%%~a
set logdir=%6
for /f "useback tokens=*" %%a in ('%logdir%') do set logdir=%%~a

set JAVA_OPTS=%3
for /f "useback tokens=*" %%a in ('%JAVA_OPTS%') do set JAVA_OPTS=%%~a


set "CATALINA_HOME=%apath%\tomcat"
set "CATALINA_BASE=%CATALINA_HOME%"
set "JRE_HOME=%8"
set "JAVA_HOME=%8"
set "EXECUTABLE=%CATALINA_HOME%\bin\tomcat7.exe"

echo Using CATALINA_HOME:    "%CATALINA_HOME%"
echo Using CATALINA_BASE:    "%CATALINA_BASE%"
echo Using JAVA_HOME:        "%JAVA_HOME%"
echo Using JRE_HOME:         "%JRE_HOME%"

set PR_DESCRIPTION=OpenESP service
set "PR_INSTALL=%EXECUTABLE%"
set "PR_LOGPATH=%CATALINA_BASE%\logs"
set "PR_CLASSPATH=%CATALINA_HOME%\bin\bootstrap.jar;%CATALINA_BASE%\bin\tomcat-juli.jar;%CATALINA_HOME%\bin\tomcat-juli.jar"
rem Set the server jvm from JAVA_HOME
set "PR_JVM=%JRE_HOME%\bin\server\jvm.dll"
if exist "%PR_JVM%" goto foundJvm
rem Set the client jvm from JAVA_HOME
set "PR_JVM=%JRE_HOME%\bin\client\jvm.dll"
if exist "%PR_JVM%" goto foundJvm
set PR_JVM=auto
:foundJvm
echo Using JVM:              "%PR_JVM%"

set serviceName=%9
set service=%7
set PR_DISPLAYNAME=%serviceName%

"%EXECUTABLE%" //IS//%serviceName% --Startup auto --StartClass org.apache.catalina.startup.Bootstrap --StopClass org.apache.catalina.startup.Bootstrap --StartParams start --StopParams stop 
"%EXECUTABLE%" //US//%serviceName% --JvmOptions "-Dcatalina.base=%CATALINA_BASE%;-Dcatalina.home=%CATALINA_BASE%;-Djava.endorsed.dirs=%CATALINA_BASE%\endorsed" --StartMode jvm --StopMode jvm 
"%EXECUTABLE%" //US//%serviceName% ++JvmOptions "-Djava.io.tmpdir=%CATALINA_BASE%\temp;-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager;-Djava.util.logging.config.file=%CATALINA_BASE%\conf\logging.properties"
"%EXECUTABLE%" //US//%serviceName% ++JvmOptions "-Xms%1M;-Xmx%1M;-Dopenesp.home=%openesphome%;-Dsolr.solr.home=%solrhome%;-Dsolr.data.dir=%datadir%;-Dsolr.log.dir=%logdir%;-Dorg.apache.manifoldcf.configfile=%openesphome%\conf\mcf\properties.xml;%JAVA_OPTS%"


SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT

set scloud=%1

if "%scloud%"=="true" (
	if "%2"=="true" goto solrcloud else goto noSolrcloud
) else (
	goto noSolrcloud
)

:solrcloud
set zkhost=%3
for /f "useback tokens=*" %%a in ('%zkhost%') do set zkhost=%%~a
set zkhost=%zkhost:+=^,%
"%EXECUTABLE%" //US//%serviceName% ++JvmOptions "-DzkRun -DzkHost=%zkhost%"

:noSolrcloud

if "%service%" == "true" goto StartService
goto DontStartService

:StartService
Net Start %serviceName%

:DontStartService
rem Clear the environment variables. They are not needed any more.
set PR_DISPLAYNAME=
set PR_DESCRIPTION=
set PR_INSTALL=
set PR_LOGPATH=
set PR_CLASSPATH=
set PR_JVM=

:end
echo DONE !