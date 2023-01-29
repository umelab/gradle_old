# How to pipe a process using exec in gradle

## Outline：  
This is a sample for Java compile options in gradle.

## Code：  
```
	project.tasks.withType(JavaCompile.class) {
		// deprecation option
		options.deprecation = true
		// encoding option
		options.encoding = 'UTF-8'
	}
```

## Link：  
[JavaCompileOptions](https://stackoverflow.com/questions/18689365/how-to-add-xlintunchecked-to-my-android-gradle-based-project)  


