package com.umelab.plugin

import org.gradle.testkit.runner.GradleRunner
import spock.lang.TempDir
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.*

class BuildLogicFunctionalTest extends Specification {

    @TempDir File testProjectDir
    File settingsFile
    File buildFile

    def setup() {
        settingsFile = new File(testProjectDir, 'settings.gradle')
        buildFile = new File(testProjectDir, 'build.gradle')
    }

    /**
    *
    *
    */
    def "test create-file task invoke"() {
        given:
        settingsFile << "rootProject.name = 'create-file'"
        buildFile << """
            plugins {
                id 'com.umelab.plugin.createfile'
            }

//        	repositories {
//				mavenCentral()
//			}
            	
            dependencies {
    			implementation 'axiom:axiom-api:1.2.12'
			}

        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('createFile')
            .withPluginClasspath()
            .build()

        then:
        //result.output.contains('Hello world!')
        result.task(":createFile").outcome == SUCCESS
    }
    // end::functional-test-classpath-setup-automatic[]
}
