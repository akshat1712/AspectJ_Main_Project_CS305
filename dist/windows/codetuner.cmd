@echo off
set "myPath=%~dp0"
set "jarPath=\codetuner-1.0-SNAPSHOT-all.jar"
set "filePath=%myPath%%jarPath%"
java -jar %filePath% %*