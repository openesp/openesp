@echo off
SET BASEDIR=%~dp0
java -jar %BASEDIR%\groovy.jar %BASEDIR%\openespctl.groovy %*
