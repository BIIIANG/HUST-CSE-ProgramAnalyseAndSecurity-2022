@echo off
if "%1" == "check" (
    echo [Java version]
    java -version
    echo [Javac version]
    javac -version
) else if "%1" == "compile" (
    if not exist bin mkdir bin
    javac -cp lib/* -d bin src/*
) else if "%1" == "instrument" (
    java -cp "bin;lib/*" MainDriver Precision FastMath FastMathCalc
) else if "%1" == "run" (
    java -cp "sootOutput;bin/;lib/*"  org.junit.runner.JUnitCore PrecisionTest
) else if "%1" == "clean" (
    if exist bin rmdir /S /Q bin
    mkdir bin
    if exist report rmdir /S /Q report
    if exist sootOutput rmdir /S /Q sootOutput
) else (
    echo Wrong syntax. Usage: script.bat check/compile/instrument/run/clean.
)
