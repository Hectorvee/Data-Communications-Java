REM Mr. A. Maganlal - CSC2A
REM Mrs. M Bowditch - CSC2B
REM Computer Science 2A/2B 2022
REM This batch file is setup to be more robust than previous versions

REM Turn echo off and clear the screen
@echo off
cls

REM Good batch file coding practice
setlocal enabledelayedexpansion

REM Move to correct folder
echo Deleting class files from bin folder
cd ..

REM Variables for batch
set ERRMSG=
set PRAC_BIN=.\bin
set PRAC_DOCS=.\docs
set PRAC_LIB=.\lib\*
set PRAC_SRC=.\src

REM Clean all class files from bin folder
:CLEAN
echo ~~~ Cleaning project ~~~
DEL /S %PRAC_BIN%\*.class
IF /I "%ERRORLEVEL%" NEQ "0" (
    set ERRMSG="Error cleaning project"
    GOTO ERROR
)

REM Move back to docs folder and wait
:END
echo ~~~ End ~~~
cd %PRAC_DOCS%
pause