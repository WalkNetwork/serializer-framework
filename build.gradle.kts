val excludeKotlin = Action<ExternalModuleDependency> {
  exclude(group = "org.jetbrains.kotlin")
}

val excludeKotlinx = Action<ExternalModuleDependency> {
  exclude(group = "org.jetbrains.kotlin")
  exclude(group = "org.jetbrains.kotlinx")
}

plugins {
  kotlin("jvm") version "1.5.20"
  kotlin("plugin.serialization") version "1.5.20"
  id("java")
  id("com.github.johnrengelman.shadow") version "6.0.0"
  id("me.bristermitten.pdm") version "0.0.33"
  id("maven-publish")
}

group = "com.github.uinnn"
version = "0.1.0"

repositories {
  mavenCentral()
  mavenLocal()
}

dependencies {
  compileOnly("com.single.api:spigot:1.8.9")
  compileOnly(kotlin("stdlib-jdk8"))
  pdm("org.jetbrains.kotlin:kotlin-reflect:1.5.20")
  pdm("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.2", excludeKotlin)
  pdm("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2", excludeKotlin)
  pdm("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.2.2", excludeKotlin)
  pdm("com.akuleshov7:ktoml-core:0.2.6")
  pdm("io.github.pdvrieze.xmlutil:serialization-jvm:0.82.0")
  pdm("io.github.pdvrieze.xmlutil:core-jvm:0.82.0", excludeKotlin)
  pdm("com.charleskorn.kaml:kaml:0.34.0")
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
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime," +
      "kotlin.ExperimentalStdlibApi," +
      "kotlinx.coroutines.DelicateCoroutinesApi," +
      "kotlinx.coroutines.ExperimentalCoroutinesApi," +
      "kotlinx.serialization.ExperimentalSerializationApi," +
      "kotlinx.serialization.InternalSerializationApi"
  }

  withType<GenerateModuleMetadata> {
    enabled = false
  }

  shadowJar {
    destinationDir = file("C:\\Users\\Cliente\\Minecraft\\Local\\plugins")
    archiveName = "${project.name}.jar"
    baseName = project.name
    version = project.version.toString()
    dependsOn(pdm)
  }
}