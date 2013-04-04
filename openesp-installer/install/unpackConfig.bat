cd %3
set apath=%1
REM remove surrounding quotes
for /f "useback tokens=*" %%a in ('%apath%') do set apath=%%~a

"%apath%\7za.exe" x %2 -aoa -y