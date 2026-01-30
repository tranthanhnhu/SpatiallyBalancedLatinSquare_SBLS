@echo off
REM Run SBLS experiments (Simple vs Improved) for n=2..max. Default max=6.
set MAX_N=6
if not "%~1"=="" set MAX_N=%~1
mvn -q exec:java -Dexec.mainClass="sbls.ExperimentRunner" -Dexec.args="%MAX_N%"
pause
