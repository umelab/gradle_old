#作成Taskでコマンドライン引数を取得する方法  

作成したタスクでコマンドライン引数を取得して、タスクの振る舞いをコントロールしたい時  
以下の２パターンが使用可能となる  
　-P フラグ　→-Psome_key=value  
　-D フラグ　→-Dsome_key=value  

ここのサンプルでは以下のコマンドで実行  
```
gradle -Psome_key=foo -Dsome_key=hoge hello  
```
