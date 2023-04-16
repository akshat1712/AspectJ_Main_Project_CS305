ajc -source 1.8 -target 1.8 Main.java Parallize.java MyService.java -outjar CODE.jar
echo "MAIN CODE JAR"
ajc  -source 1.8 -target 1.8 -cp "$ASPECT_PATH" ParallizeAspect.java Parallize.java -outjar ASPECT.jar
echo "ASPECT CODE JAR"

ajc -inpath CODE.jar -aspectpath ASPECT.jar
echo "WEAVED CODE JAR"

java -cp "./:$ASPECT_PATH:./ASPECT.jar" Main
echo "FINAL RUNNING"
