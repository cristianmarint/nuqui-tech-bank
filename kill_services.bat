@echo off
REM Define the project root directory
set PROJECT_ROOT=D:\github\nuqui-tech

REM Read the pids.txt file line by line
for /F "tokens=1,2 delims=:" %%A in (%PROJECT_ROOT%\pids.txt) do (
    echo '-------- stopping %%A'
    taskkill /F /PID %%B
)

REM Delete the pids.txt file
del %PROJECT_ROOT%\pids.txt