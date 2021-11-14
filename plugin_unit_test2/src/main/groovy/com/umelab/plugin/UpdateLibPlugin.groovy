package com.umelab.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.artifacts.dsl.RepositoryHandler

import groovy.io.FileType

class UpdateLibPlugin implements Plugin<Project> {
    
    //custom task name
    def TASKNAME_CREATE_FILES = 'updateLib'
    
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

			doLast {
				def jarFile
				project.file(libDir).traverse(type: FileType.FILES, maxDepth :0) {
					jarFile = it.path
					renamed = renameFiles(jarFile)
					//ファイル削除
					project.file(renamed).delete
					//ファイルリネーム
					project.file(jarFile).renameTo(project.file(renamed))
				}
			}
    	}
    }

	def renameFiles(String filename, Project project) {
		//バージョン番号を削除する
		def convFilename = filename.replaceAll(/(-[0-9]{1,2})(.[0-9]{1,2})*(.[0-9]{1,4})*((-SNAPSHOT)|(.[0-9a-zA-Z]*))*.jar/, ".jar")
		
		if (filename != convFilename) {
			//元に存在するJarファイルを削除する
			def existFilename = project.file(convFilename)
			if (existFilename.exists()) {
				project.delete existFilename
			}
			project.file(filename).renameTo(project.file(convFilename))
		}
	}
    
    def configureDependency(Project project) {
    	def artifact_url = 'https://repo.maven.apache.org/maven2'
		def artifact_user = ''
    	def artifact_token = ''
    	project.repositories {
			maven {
				url (artifact_url)
			}
		}

	}
}
