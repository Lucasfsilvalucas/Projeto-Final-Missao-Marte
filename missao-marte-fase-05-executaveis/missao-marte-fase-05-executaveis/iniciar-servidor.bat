@echo off
cd /d "%~dp0servidor"
if defined JAVA_HOME (
    "%JAVA_HOME%\bin\java.exe" -jar "missao-marte-servidor-0.5.0.jar"
) else (
    java -jar "missao-marte-servidor-0.5.0.jar"
)
pause
