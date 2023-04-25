ASPECT_PATH=../../src/main/resources/aspectjrt.jar
ajc -source 1.9 -target 1.9 Main.java Logger.java  InstanceLog.java MyService.java -outjar CODE.jar
ajc  -source 1.9 -target 1.9 -cp "$ASPECT_PATH" LogAspect.java Logger.java InstanceLog.java -outjar ASPECT.jar
ajc -inpath CODE.jar -aspectpath ASPECT.jar
java -cp "./:$ASPECT_PATH:./ASPECT.jar" Main
