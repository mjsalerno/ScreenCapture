@echo off
cd src
javac screencapture\*.java
echo Main-Class: screencapture.ScreenCapture > man.txt
jar cfm ScreenCapture.jar man.txt screencapture
del man.txt
cd screencapture
del *.class
cd ..\..
copy src\ScreenCapture.jar .
del src\ScreenCapture.jar
pause