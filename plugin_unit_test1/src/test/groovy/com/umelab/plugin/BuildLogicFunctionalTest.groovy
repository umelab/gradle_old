package com.umelab.plugin

import org.gradle.testkit.runner.GradleRunner
import spock.lang.TempDir
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.*
import java.io.File

class BuildLogicFunctionalTest extends Specification {
	//テストが終了すると自動的に削除される
	//C:\Users\u6965\AppData\Local\Temp\spock_test_XXXXに作成される
    @TempDir File testProjectDir
    File settingsFile
    File buildFile
	File propFile
	
    def setup() {
        settingsFile = new File(testProjectDir, 'settings.gradle')
        buildFile = new File(testProjectDir, 'build.gradle')
    	propFile = new File(testProjectDir, 'gradle.properties')
    	//settings.gradle
        settingsFile << "rootProject.name = 'create-file'"
		//gradle.properties
		propFile << """
			systemProp.http.proxyHost=10.0.200.155
			systemProp.http.proxyPort=8080
			systemProp.https.proxyHost=10.0.200.155
			systemProp.https.proxyPort=8080
		"""
		//build.gradle
        buildFile << """
            plugins {
                id 'com.umelab.plugin.createfile'
            }

            dependencies {
    			implementation 'org.postgresql:postgresql:42.2.14'
    			//testImplementation 'org.apache.logging.log4j:log4j:2.14.1'
    			testImplementation 'org.objenesis:objenesis:3.2'
			}
        """
    }

    /**
    *
    *  createFileタスクの起動テスト
    */
    def "test for invoking create-file task"() {
        when:
        // BuildResult API https://docs.gradle.org/current/javadoc/org/gradle/testkit/runner/BuildResult.html
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('createFile')
            .withPluginClasspath()
            .withDebug(true)				//Debugするか？
            //.withEnvironment(new HashMap<String,​String> environmentVariables)
            .build()

        then:
		//タスクが起動されているか
        println result.task(":createFile").path
        result.task(":createFile").outcome == SUCCESS

//		  テスト結果をANDで連結可能
//        and:
//        File outFile = new File(testProjectDir, 'lib/postgresql-42.2.14.jar')
//        assert outFile.exists()
    }
    
    /**
    *  implementation: ファイルコピーテスト
    */
    def "test for copying file with implementation"() {
    	when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('createFile')
            .withPluginClasspath()
            .build()
    	then:
		File outFile = new File(testProjectDir, 'lib/postgresql-42.2.14.jar')
        assert outFile.exists()
	}
	
    /**
    *  testImplementation: ファイルコピーテスト
    */
    def "test for copying file with testImplementation"() {
    	when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('createFile')
            .withPluginClasspath()
            .build()
    	then:
		File outFile = new File(testProjectDir, 'lib/objenesis-3.2.jar')
        assert outFile.exists()
	}
	
}
