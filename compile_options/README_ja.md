# How to pipe a process using exec in gradle

## 概要：  
GradleでJavaコンパイル・実行時にプロパティ・パラメータを指定したい場合のサンプルを記述する。  

## コード：  
```
	project.tasks.withType(JavaCompile.class) {
		// deprecation option
		options.deprecation = true
		// encoding option
		options.encoding = 'UTF-8'
	}
```

## 参照リンク：  
[Javaコンパイルオプション](https://stackoverflow.com/questions/18689365/how-to-add-xlintunchecked-to-my-android-gradle-based-project)  
