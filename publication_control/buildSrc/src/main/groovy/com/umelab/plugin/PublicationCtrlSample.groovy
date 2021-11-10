package com.umelab.plugin

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.provider.Property
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository

import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Bundling
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.Usage
import org.gradle.api.component.AdhocComponentWithVariants

/**
*
* title: Publication control sample
* description: this is a sample plugin code when you want to skip publication in multi-project
* in this example, app project is skip to publish but other project is upload artifacts.
*/
class PublicationCtrlSample implements Plugin<Project> {

	private static final String TASK_NAME_SRCJAR = 'srcJar'
	private static final String TASK_NAME_PUBCTL = 'pubCtrl'
	private static final String TASK_NAME_CREATESRCJAR = 'createSrcJar'
	
	private static final String PROPKEY_PROJECT_SKIP = 'PublicationSkipProjName'

	/**
	 * 
	 * {@inheritDoc}
	 * @see org.gradle.api.Plugin#apply(java.lang.Object)
	 */
	@Override
	void apply(Project project) {
		String url = System.getProperty("ARTIFACTS_URL")
		String user = System.getProperty("USER") != null ? System.getProperty("USER") : 'user'
		String passwd = System.getProperty("PASSWD") != null ? System.getProperty("PASSWD") : 'pass'
		String version = System.getProperty("VERSION") != null ? System.getProperty("VERSION") : '1.0.0-SNAPSHOT'

		applyPlugin(project)
		configurePublish(project, url, passwd, 'groupId',  version)
		configureSrcJar(project)
		registerTasks(project)
	}

	/**
	 * 
	 * 必要なプラグインを適用します。
	 *
	 * @param project
	 */
	private void applyPlugin(Project project) {
		project.getPlugins().apply(JavaPlugin.class)
		project.getPlugins().apply(MavenPublishPlugin.class)
	}

	private void configureSrcJar(Project project) {
//		def tasknames = project.gradle.startParameter.getTaskNames()
//		if (tasknames.contains(TASK_NAME_SRCJAR)) {
			if (project == project.rootProject) {
				Configuration config = createConfiguration(project)
				def tmpTask = project.tasks.register(TASK_NAME_CREATESRCJAR, Jar) {
					println '-- task called --'
					def allsrc = project.sourceSets.main.allJava
					archiveBaseName = project.archivesBaseName + '-src'
					classifier = 'sources'
					dependsOn("classes")
					doFirst {
						project.subprojects.each {
							allsrc += it.sourceSets.main.allJava
						}
					}
					from allsrc
					duplicatesStrategy = 'include'
					onlyIf {
						project == project.rootProject
					}
				}

				println 'proj: ' + project.name + ' task: ' + tmpTask.name
				config.outgoing.artifact(tmpTask)
				addVariantToComponent(project, config)
			}
//		}
	}

	private void addVariantToComponent(Project project, Configuration outgoing) {
        AdhocComponentWithVariants javaComponent = (AdhocComponentWithVariants) project.components.findByName("java")
        //def javaComponent = softwareComponentFactory.adhoc("mySrcComponent")

        // add it to the list of components that this project declares
        project.components.add(javaComponent)

        javaComponent.addVariantsFromConfiguration(outgoing) {
            it.mapToMavenScope("runtime")
            it.mapToOptional()
        }
    }

	private Configuration createConfiguration(Project project) {
        project.configurations.create(TASK_NAME_CREATESRCJAR) { Configuration cnf ->
            cnf.canBeConsumed = true
            cnf.canBeResolved = false
            cnf.attributes {
                it.attribute(Category.CATEGORY_ATTRIBUTE, project.objects.named(Category, Category.LIBRARY))
                it.attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage, Usage.JAVA_RUNTIME))
                it.attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, project.objects.named(LibraryElements, 'src-jar'))
            }
        }
    }
	
	
	/**
	 * 
	 * タスクを登録します。
	 *
	 * @param project
	 * @param extension
	 */
	private void registerTasks(Project project) {
		project.tasks.register(TASK_NAME_SRCJAR) {
			dependsOn("classes")
		}
		// srcJarタスク

		// pubCtlJarタスク
		project.tasks.register(TASK_NAME_PUBCTL) {
			def targets = project.property(PROPKEY_PROJECT_SKIP).split(",")
			def obj = []
			dependsOn("classes")
			doLast {
				project.subprojects.each { proj ->
					println 'sub proj: ' + proj.name
					targets.each {
						// if project name equals app which is defined in 'PublicationSkipProjName' in gradle.properties
						if (proj.name.equals(it)) {
							//proj.model1 {
							println 'delete proj: ' + it
							proj.tasks.generatePomFileForMavenPublication.enabled = false
							proj.tasks.publishMavenPublicationToMavenRepository.enabled = false
							proj.tasks.publish.enabled = false
						}
					}
				}
			}
		}
	}
	
	protected void configurePublish(Project project, String artifact_url, String artifact_token, String group_name, String version) {
		project.group = group_name
		project.version = version
		project.publishing {
			publications {
				maven(MavenPublication.class) {
					groupId = group_name
					version = version
					from project.components.java
				}
			}
			repositories {
				maven {
					url (artifact_url)
					credentials {
						username ('')
						password (artifact_token)
					}
				}
			}
		}
	}

}
