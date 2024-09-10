REM Mr. A. Maganlal
REM Computer Science 2A 2015-2024
REM This batch file is setup to be more robust than previous versions.

REM Turn echo off and clear the screen.
@echo off
cls

REM Good batch file coding practice.
setlocal enabledelayedexpansion

REM Paths for JDK
REM Remember to change JAVA_HOME to the correct path on your system
echo Change JAVA_HOME path

set JAVA_HOME="C:\jdk-21"
set PATH=%JAVA_HOME%\bin;%PATH%
REM Paths for JavaFX
set USE_JAVAFX=true
set JAVAFX_HOME="C:\javafx-sdk-21"
set JAVAFX_MODULES=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media
set JAVAFX_ARGS=
if %USE_JAVAFX%==true (set JAVAFX_ARGS=--module-path %JAVAFX_HOME%\lib --add-modules=%JAVAFX_MODULES%)
echo %USE_JAVAFX%, %JAVAFX_ARGS%

REM Variable for error messages
set ERRMSG=

:VERSION
echo ~~~ Checking Version ~~~
javac -version
IF /I "%ERRORLEVEL%" NEQ "0" (
    set ERRMSG="Error checking version"
    GOTO ERROR
)
java -version
IF /I "%ERRORLEVEL%" NEQ "0" (
    set ERRMSG="Error checking version"
    GOTO ERROR
)

pause
REM Move to correct folder.
echo Build script set to run in Project folder
cd ..

REM Variables for batch
set PRAC_BIN=.\bin
set PRAC_DOC=.\docs
set PRAC_JDC=.\JavaDoc
set PRAC_LIB=.\lib\*
set PRAC_SRC=.\src

REM Clean all class files from bin folder and the JavaDocs folder from docs foler.
:CLEAN
echo ~~~ Cleaning project ~~~
DEL /S %PRAC_BIN%\*.class
RMDIR /Q /S %PRAC_DOC%\%PRAC_JDC%
IF /I "%ERRORLEVEL%" NEQ "0" (
    echo ~~! Error cleaning project !~~
)

REM Compile project by compiling just Main. Main will reference required classes.
:COMPILE
echo ~~~ Compiling project ~~~
javac %JAVAFX_ARGS% -sourcepath %PRAC_SRC% -cp %PRAC_BIN%;%PRAC_LIB% -d %PRAC_BIN% %PRAC_SRC%\Main.java
IF /I "%ERRORLEVEL%" NEQ "0" (
    set ERRMSG=~~! Error compiling project !~~
    GOTO ERROR
)

REM Generate JavaDoc for project for only acsse subpackage.
:JAVADOC
echo ~~~ Generate JavaDoc for project ~~~
javadoc %JAVAFX_ARGS% -sourcepath %PRAC_SRC% -classpath %PRAC_BIN%;%PRAC_LIB% -use -version -author -d %PRAC_DOC%\%PRAC_JDC% -subpackages acsse
IF /I "%ERRORLEVEL%" NEQ "0" (
    echo ~~! Error generating JavaDoc for project !~~
)

REM Run project by running Main. Any arguments passed to the batch file are passed to Main.
:RUN
echo ~~~ Running project ~~~
java %JAVAFX_ARGS% -cp %PRAC_BIN%;%PRAC_LIB% Main %*
IF /I "%ERRORLEVEL%" NEQ "0" (
    set ERRMSG=~~! Error running project !~~
    GOTO ERROR
)
GOTO END

REM Something went wrong, display error.
:ERROR
echo ~~! Fatal error with project !~~
echo %ERRMSG%

REM Move back to docs folder and wait.
:END
echo ~~~ End ~~~
cd %PRAC_DOCS%
pause
