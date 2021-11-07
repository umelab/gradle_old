package com.umelab.plugin

import groovy.io.FileType
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.provider.Property
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.StopExecutionException

class CompileOptionSample implements Plugin<Project> {

	@Override
	void apply(Project project) {
		applyPlugins(project)
		configureJavaCompile(project)
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
	*	override JavaCompile task
	*	@param project
	*/
	private void configureJavaCompile(Project project) {
		project.tasks.withType(JavaCompile.class) {
			// set ja-lang
			options.fork = true
			options.forkOptions.jvmArgs = ["-Duser.language=ja"]
			// -Xlint option
			options.compilerArgs << '-Xlint:unchecked'
			// deprecation option
			options.deprecation = true
			// encoding option
			options.encoding = 'UTF-8'
		}
	}
}