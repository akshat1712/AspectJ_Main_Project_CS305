ASPECT_PATH=../../src/main/resources/aspectjrt.jar
ajc -source 1.8 -target 1.8 Main.java Parallelize.java MyService.java -outjar CODE.jar
#ajc  -source 1.8 -target 1.8 -cp "$ASPECT_PATH" ParallizeAspect.java Parallelize.java -outjar ASPECT.jar
ajc -source 1.8 -target 1.8  -inpath CODE.jar ParallizeAspect.java -outjar _weavesd.jar
java -cp "./:$ASPECT_PATH:./ASPECT.jar" Main
