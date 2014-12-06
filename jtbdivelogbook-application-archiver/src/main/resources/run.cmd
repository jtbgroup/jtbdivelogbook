@echo off 
set LIB_CLASSPATH=
set LIB_PATH=
set LIB_DIR=.\lib
set BIN_DIR=.\bin\windows

echo ===============================================================================
echo.
echo	Jt'B Dive Logbook
echo	Developped by Jt'B team
echo.
echo	LIB_DIR: %LIB_DIR%
echo	BIN_DIR: %BIN_DIR%
echo.
echo ===============================================================================
echo.

java -version

rem set the path
set PATH=%PATH%;%BIN_DIR%
echo PATH is : %PATH%
echo.

rem set the classpath
FOR %%f IN (%LIB_DIR%\*.jar) DO (call :append_classpath %%f)
set CLASSPATH=%CLASSPATH%;%LIB_CLASSPATH%
echo CLASSPATH is : %CLASSPATH%

start javaw ${mainClass}



:append_classpath
rem echo LIB_CLASSPATH:%LIB_CLASSPATH% - arg:%1
set LIB_CLASSPATH=%LIB_CLASSPATH%;%1
