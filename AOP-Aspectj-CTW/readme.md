# Before you start
run `mvn clean install` first, then start the app.
and you will see log like:
```dtd
[INFO] Join point 'method-execution(void com.lomoy.aop.MyService.privateMethodB())' in Type 'com.lomoy.aop.MyService' (MyService.java:30) advised by before advice from 'com.lomoy.aop.MyLogAspect' (MyLogAspect.java:18)
```
this means the method is woven successfully.

# AspectJ CTW ( compile time weavering)
This does not require `-javaagent` inject into VM.



# Java
I'm using java 11, and set module's `language level` to 8.
I don't know how it works, but it works.

# Pom
Take a look of the `dependencies` and `plugins`, CTW requires AspectJ complier engaged in the compiling.
Because, it needs to do bytecode enhance during compiling time.