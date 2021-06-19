package bar

import org.gradle.api.invocation.Gradle
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.bundling.Jar

class JarSrcPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		//apply JavaPlugin for implementation
		project.getPlugins().apply(JavaPlugin.class)
		
		project.afterEvaluate {
		//Gradle gradle = project.getGradle()
		//gradle.projectsEvaluated {
			 println '-- projectEvaluated: ' + project.name
			 project.tasks.named(JavaPlugin.JAR_TASK_NAME, Jar) {
			 	println '-- inside jar ' + project.name
				from sourceSets.main.allSource
			}
		}
	}
}