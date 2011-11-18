cd src
javac screencapture\*.java
echo Main-Class: screencapture.ScreenCapture > man.txt
jar cfm ScreenCapture.jar man.txt screencapture
rm man.txt
cd screencapture
rm *.class
cd ..\..
cp src\ScreenCapture.jar .
rm src\ScreenCapture.jar
pause