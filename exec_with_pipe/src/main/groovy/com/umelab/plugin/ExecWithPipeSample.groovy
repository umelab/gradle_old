package com.umelab.plugin

import groovy.io.FileType
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.provider.Property
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.bundling.Jar

class ExecWithPipeSample implements Plugin<Project> {

	@Override
	void apply(Project project) {
		applyPlugins(project)
		configureJar(project)
	}
	
	/**
	*	apply JavaPlugin and MavenPublishPlugin
	*	@param project
	*/
	private void applyPlugins(Project project) {
		//apply JavaPlugin
		project.getPlugins().apply(JavaPlugin.class)
		//apply MavenPublishPlugin
		project.getPlugins().apply(MavenPublishPlugin.class)
	}
	
	/**
	*	override Jar task, edits MANIFEST files with git branch and commit id
	*	@param project
	*/
	private void configureJar(Project project) {
		project.tasks.withType(Jar) {
			doFirst {
				def branch
				def revno
				def stdout
				// get commit id
				project.exec {
					commandLine 'git', 'rev-parse', 'HEAD'
					standardOutput = new ByteArrayOutputStream();
					stdout = standardOutput
				}
				revno = stdout.toString().trim();
				// get default branch
				// if you want to use "|", need to put /bin/sh with -c and then puts parameters
				// https://stackoverflow.com/questions/3776195/using-java-processbuilder-to-execute-a-piped-command
				project.exec {
					commandLine '/bin/sh', '-c', 'git remote show origin  | grep \'HEAD branch\' | awk \'{print $NF}\''
					standardOutput = new ByteArrayOutputStream();
					stdout = standardOutput
				}
				branch = stdout.toString().trim();
				// write to MANIFEST.MF
				manifest {
					attributes("Archive-Version": version,
											"Built-Branch": branch,
											"Built-Revision": revno)
				}
			}
		}
	}
}