package com.umelab.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.artifacts.dsl.RepositoryHandler

class CreateFilePlugin implements Plugin<Project> {
    
    //custom task name
    def TASKNAME_CREATE_FILES = 'createFile'
    
	/**
	 * 
	 * {@inheritDoc}
	 * @see org.gradle.api.Plugin#apply(java.lang.Object)
	 */
//	@Override
    void apply(Project project) {
    	project.getPlugins().apply(JavaPlugin.class)
    	configureDependency(project)
		registerTask(project)
    }
    
    /**
    * define tasks
    *
    * @param project
    */
    def registerTask(Project project) {
    	project.tasks.register(TASKNAME_CREATE_FILES) {
    		def libDir = 'lib'
    		
    		doFirst {
    			if (!project.file(libDir).exists()) {
    				project.mkdir(libDir)
    			}
    			project.copy {
    				//from project.configurations.compileClasspath
    				from project.configurations.testCompileClasspath
    				include '*.jar'
    				into libDir
    			}
    		}
    	}
    }
    
    def configureDependency(Project project) {
    	def artifact_url = 'https://repo.maven.apache.org/maven2'
		def artifact_user = ''
    	def artifact_token = ''
    	project.repositories {
			maven {
				url (artifact_url)
/**				credentials {
					username (artifact_user)
					password (artifact_token)
				}
**/
			}
		}

	}
}
