ASPECT_PATH=../../src/main/resources/aspectjrt.jar
ajc -source 1.8 -target 1.8 Main.java Parallize.java MyService.java -outjar CODE.jar
ajc  -source 1.8 -target 1.8 -cp "$ASPECT_PATH" ParallizeAspect.java Parallize.java -outjar ASPECT.jar
ajc -inpath CODE.jar -aspectpath ASPECT.jar
java -cp "./:$ASPECT_PATH:./ASPECT.jar" Main
