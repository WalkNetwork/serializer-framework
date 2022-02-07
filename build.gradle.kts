plugins {
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.serialization") version "1.6.10"
	id("java")
	id("com.github.johnrengelman.shadow") version "7.0.0"
	id("maven-publish")
	id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
	id("signing")
}

group = "io.github.uinnn"
version = "2.5.0"

repositories {
	mavenCentral()
	mavenLocal()
}

dependencies {
	api("io.github.uinnn:walk-server:2.4.0")
	api(kotlin("stdlib-jdk8"))
	api("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
	api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
	api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
	api("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.3.2")
	api("com.charleskorn.kaml:kaml:0.40.0")
	api("net.benwoodworth.knbt:knbt:0.11.1")
}

nexusPublishing {
	repositories {
		sonatype {
			nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
			snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
		}
	}
}

tasks {
	publishing {
		repositories {
			maven {
				url = uri("https://repo.maven.apache.org/maven2/")
			}
		}
		
		publications {
			create<MavenPublication>("maven") {
				from(project.components["kotlin"])
				
				val sourcesJar by creating(Jar::class) {
					archiveClassifier.set("sources")
					from(sourceSets.main.get().allSource)
				}
				
				val javadocJar by creating(Jar::class) {
					dependsOn.add(javadoc)
					archiveClassifier.set("javadoc")
					from(javadoc)
				}
				
				setArtifacts(listOf(sourcesJar, javadocJar, jar))
				
				groupId = "io.github.uinnn"
				artifactId = "serializer-framework"
				version = project.version.toString()
				pom {
					name.set("serializer-framework")
					description.set("A Serializer framework made in kotlin for standalone/spigot use!")
					url.set("https://github.com/uinnn/serializer-framework")
					developers {
						developer {
							id.set("uinnn")
							name.set("Uin Carrara")
							email.set("uin.carrara@gmail.com")
						}
					}
					licenses {
						license {
							name.set("MIT Licenses")
						}
					}
					scm {
						url.set("https://github.com/uinnn/serializer-framework/tree/master/src")
					}
				}
			}
		}
	}
	
	signing {
		sign(publishing.publications["maven"])
	}
	
	compileKotlin {
		kotlinOptions.freeCompilerArgs +=
			"-Xopt-in=kotlin.ExperimentalStdlibApi," +
				"kotlinx.serialization.ExperimentalSerializationApi," +
				"kotlinx.serialization.InternalSerializationApi," +
				"kotlin.experimental.ExperimentalTypeInference," +
				"kotlin.contracts.ExperimentalContracts," +
				"kotlin.time.ExperimentalTime"
	}
	
	shadowJar {
		destinationDir = file("C:\\Users\\Cliente\\Minecraft\\Local\\plugins")
		archiveName = "${project.name}.jar"
		baseName = project.name
		version = project.version.toString()
	}
}
