@echo off
setlocal
cd /d "%~dp0"
if exist "gradle\wrapper\gradle-wrapper.jar" (
  java -classpath "gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
  exit /b %errorlevel%
)
where gradle >nul 2>nul
if not errorlevel 1 (
  echo AVISO: Wrapper JAR ausente; usando Gradle instalado.
  echo Execute gradle wrapper --gradle-version 8.14.3 para completar o Wrapper.
  gradle %*
  exit /b %errorlevel%
)
echo Gradle Wrapper incompleto: falta gradle\wrapper\gradle-wrapper.jar
 echo Instale Gradle e execute: gradle wrapper --gradle-version 8.14.3
exit /b 1
