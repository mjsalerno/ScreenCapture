cd src
<<<<<<< HEAD
javac screencapture/*.java
=======
javac screencapture\*.java
>>>>>>> added a make script for linux and windows
echo Main-Class: screencapture.ScreenCapture > man.txt
jar cfm ScreenCapture.jar man.txt screencapture
rm man.txt
cd screencapture
rm *.class
<<<<<<< HEAD
cd ../..
cp src/ScreenCapture.jar .
rm src/ScreenCapture.jar
chmod +x ScreenCapture.jar
echo press any key to exit.
read w
=======
cd ..\..
cp src\ScreenCapture.jar .
rm src\ScreenCapture.jar
pause
>>>>>>> added a make script for linux and windows
