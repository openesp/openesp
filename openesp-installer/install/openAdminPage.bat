@ECHO OFF
echo.
echo    The OpenESP admin page is located at:
echo. 
echo    http://localhost:${solr.port.number}/ 
echo. 
echo    The admin page will automatically open in a few seconds ...
echo.
echo    ... Press Next to finish the install.
ping a.b.c.d -n 1 -w 5000 > nul
verify > nul
start /B http://localhost:${solr.port.number}/
verify > nul
exit
