@echo off

rem The HEAP_SIZE variable line defines the Java heap size in MB. 
rem That is the total amount of memory available to Guineu.
rem Please adjust according to the amount of memory of your computer.
rem Maximum value on a 32-bit Windows system is about 1300. 
set HEAP_SIZE=1024

rem The TMP_FILE_DIRECTORY parameter defines the location where temporary 
rem files (parsed raw data) will be placed. Default is %TEMP%, which 
rem represents the system temporary directory.
set TMP_FILE_DIRECTORY=%TEMP%

rem This is the home directory path for NIST MS Search.  It should hold
rem nistms$.exe.
set NIST_MS_SEARCH_PATH=C:\NISTDEMO\MSSEARCH


rem Set R environment variables.
set R_HOME=C:\Program Files\R\R-2.12.0
set R_SHARE_DIR=%R_HOME%\share 
set R_INCLUDE_DIR=%R_HOME%\include
set R_DOC_DIR=%R_HOME%\doc
set R_LIBS_USER=%USERPROFILE%\Documents\R\win-library\2.12

rem Include R DLLs in PATH.
set PATH=%PATH%;%R_HOME%\bin\i386

rem The directory holding the JRI JARs (JRI.jar, JRIEngine.jar REngine.jar).
set JRI_CLASS_PATH=%R_LIBS_USER%\rJava\jri

rem The directory holding the JRI shared library (libjri.so).
set JRI_LIB_PATH=%JRI_CLASS_PATH%\i386

rem It is usually not necessary to modify the JAVA_COMMAND parameter, but 
rem if you like to run a specific Java Virtual Machine, you may set the 
rem path to the java command of that JVM
set JAVA_COMMAND=java

rem It is not necessary to modify the following section
set LOGGING_CONFIG_FILE=conf/logging.properties
set JAVA_PARAMETERS=-XX:+UseParallelGC -Djava.io.tmpdir=%TMP_FILE_DIRECTORY% -Djava.util.logging.config.file=%LOGGING_CONFIG_FILE% -Xms%HEAP_SIZE%m -Xmx%HEAP_SIZE%m -Dnist.ms.search.path="%NIST_MS_SEARCH_PATH%" -Djava.library.path="%JRI_LIB_PATH%"
set CLASS_PATH=Guineu.jar;"%JRI_CLASS_PATH%\JRIEngine.jar";"%JRI_CLASS_PATH%\JRI.jar";"%JRI_CLASS_PATH%\REngine.jar"
set MAIN_CLASS=guineu.main.GuineuCore

rem Show java version, in case a problem occurs
%JAVA_COMMAND% -version

rem This command starts the Java Virtual Machine
%JAVA_COMMAND% %JAVA_PARAMETERS% -classpath %CLASS_PATH% %MAIN_CLASS%  

rem If there was an error, give the user chance to see it
IF ERRORLEVEL 1 pause


