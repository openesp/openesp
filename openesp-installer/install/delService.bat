@echo off
SET BASEDIR=%~dp0

"%BASEDIR%\uninstall-windows-service.bat" -s ${openesp.service.name}

