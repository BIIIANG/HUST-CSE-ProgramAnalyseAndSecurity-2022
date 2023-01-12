@echo off
if "%1" == "check" (
    echo [Java version]
    java -version
    echo [Javac version]
    javac -version
) else if "%1" == "compile" (
    if not exist bin\main mkdir bin\main
    if not exist bin\test mkdir bin\test
    javac -O -cp "lib/*;src/main/" -d bin/main src\main\org\apache\commons\math3\util\*
    javac -cp "lib/*;src/" -d bin/ src\*.java
    javac -cp "lib/*;src/main/;src/test/" -d bin/test src\test\org\apache\commons\math3\util\*
) else if "%1" == "instrument" (
    java -cp "bin;lib/*" MainDriver
) else if "%1" == "run" (
    java -cp "sootOutput;lib/*;bin/main;bin/test;bin" -DtestSuiteName="FastMathTest" org.junit.runner.JUnitCore org.apache.commons.math3.util.FastMathTest org.apache.commons.math3.util.PrecisionTest org.apache.commons.math3.util.MathArraysTest
) else if "%1" == "clean" (
    if exist bin rmdir /S /Q bin
    mkdir bin
    if exist report rmdir /S /Q report
    if exist sootOutput rmdir /S /Q sootOutput
) else (
    echo Wrong syntax. Usage: script.bat check/compile/instrument/run/clean.
)
