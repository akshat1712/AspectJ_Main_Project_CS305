export ASPECT_PATH="../../src/main/resources/aspectjrt.jar"
ajc -source 1.9 -target 1.9 Main.java Cached.java MyService.java -outjar CODE.jar
# ajc  -source 1.9 -target 1.9 -cp "$ASPECT_PATH" StringCachingAspect.java Cached.java -outjar ASPECT.jar
ajc -inpath CODE.jar -source 1.9 -target 1.9 StringCachingAspect.java -outjar ASPECT.jar
java -cp "./:$ASPECT_PATH:./ASPECT.jar" Main