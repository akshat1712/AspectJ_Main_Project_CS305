ajc -source 1.9 -target 1.9 Main.java CPUMemoryUsage.java MyService.java -outjar CODE.jar
echo "MAIN CODE JAR"
ajc  -source 1.9 -target 1.9 -cp "$ASPECT_PATH" CPUMemoryUsageAspect.java CPUMemoryUsage.java -outjar ASPECT.jar
echo "ASPECT CODE JAR"

ajc -inpath CODE.jar -aspectpath ASPECT.jar
echo "WEAVED CODE JAR"

java -cp "./:$ASPECT_PATH:./ASPECT.jar" Main
echo "FINAL RUNNING"
