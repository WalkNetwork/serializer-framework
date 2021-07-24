plugins {
  kotlin("jvm") version "1.5.20"
  kotlin("plugin.serialization") version "1.5.20"
  id("java")
  id("com.github.johnrengelman.shadow") version "6.0.0"
  id("maven-publish")
}

group = "com.github.uinnn"
version = "1.2"

repositories {
  mavenCentral()
  mavenLocal()
}

dependencies {
  compileOnly("com.single.api:spigot:1.8.9")
  compileOnly(kotlin("stdlib-jdk8"))
  compileOnly("org.jetbrains.kotlin:kotlin-reflect:1.5.20")
  compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.2")
  compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
  compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.2.2")
  compileOnly("com.charleskorn.kaml:kaml:0.34.0")
}

tasks {
  publishing {
    publications {
      create<MavenPublication>("maven") {
        from(project.components["kotlin"])
        groupId = "com.github.uinnn"
        artifactId = "serializer-framework"
        version = project.version.toString()
      }
    }
  }

  compileKotlin {
    kotlinOptions.freeCompilerArgs +=
      "-Xopt-in=kotlin.ExperimentalStdlibApi," +
      "kotlinx.serialization.ExperimentalSerializationApi," +
      "kotlinx.serialization.InternalSerializationApi"
  }

  shadowJar {
    destinationDir = file("C:\\Users\\Cliente\\Minecraft\\Local\\plugins")
    archiveName = "${project.name}.jar"
    baseName = project.name
    version = project.version.toString()
  }
}