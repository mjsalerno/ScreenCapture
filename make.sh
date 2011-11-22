cd src
javac screencapture/*.java
echo Main-Class: screencapture.ScreenCapture > man.txt
jar cfm ScreenCapture.jar man.txt screencapture
rm man.txt
cd screencapture
rm *.class
cp src/ScreenCapture.jar .
rm src/ScreenCapture.jar
chmod +x ScreenCapture.jar
echo press any key to exit.
read w
