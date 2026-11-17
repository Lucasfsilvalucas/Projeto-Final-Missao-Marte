@echo off
cd /d "%~dp0cliente"
call bin\lwjgl3.bat
if errorlevel 1 pause
