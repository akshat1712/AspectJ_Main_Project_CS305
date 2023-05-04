# Codetuner
A java tool that can be used to modify the java bytecode for logging, caching, profiling, making methods concurrent without changing the source code.

## Depedencies
- [Java 18](https://www.oracle.com/java/technologies/javase-downloads.html)
- [AspectJ](https://www.eclipse.org/aspectj/)

Note: The project is built using Java 18, It may or may not work with other distributions. AspectJ is not necessarily required to run the project, but it may be useful to compile java code using ajc so that the variables are not renamed by the compiler. This will help in logging the variables.

## Build
- Make sure you are on the root directory of the project and the dependencies are installed.
- Run the following commands to build the project. This will also generate jacoco test coverage report which will be available at docs/report/
```js
./gradlew build
```
- Now run the following command to generate the jar file and save it in necessary directory inside the dist folder.
```
./gradlew shadowJar
```

Note: If ./gradlew doesn't work, try to run gradlew instead. Also, in some systems, neccessary permissions may not be available to run the gradlew file. In that case, run the following command to give the necessary permissions.
```
chmod +x gradlew
```

## Usage
- Follow the README present inside the dist for your system to install the tool.
- Run the following command to see the usage of the tool.
```
codetuner --help
```
- This will show the help menu.
- Follow the help menu and manual in the docs/ to use the tool.