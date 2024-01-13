@echo off
REM Define the project root directory
set PROJECT_ROOT=D:\github\nuqui-tech

REM Iterate over each subdirectory in the project root directory
for /D %%D in ("%PROJECT_ROOT%\*") do (
    echo '-------- compiling and running %%~nxD'
    cd %%D && gradlew clean build
    start /B /MIN cmd /C "java -jar ./build/libs/%%~nxD-0.0.1-SNAPSHOT.jar > nul 2>&1"
)

REM Iterate over each subdirectory in the project root directory again to get the PIDs
for /D %%D in ("%PROJECT_ROOT%\*") do (
    for /F %%i in ('wmic process where "commandline like '%%-jar ./build/libs/%%~nxD-0.0.1-SNAPSHOT.jar%%' and not commandline like '%%wmic%%' get processid ^| findstr [0-9]'') do (
        echo %%~nxD: %%i >> %PROJECT_ROOT%\pids.txt
    )
)