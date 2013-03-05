@echo off
SET BASEDIR=%~dp0

java -jar "%BASEDIR%\groovy.jar" "%BASEDIR%\uninstall-windows-service.groovy" %*