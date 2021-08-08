plugins {
    idea
    kotlin("jvm") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("plugin.serialization") version "1.4.32"
}

group = "si.budimir"
version = "1.4.0"

repositories {
    mavenCentral()
    maven {  url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    compileOnly("io.github.waterfallmc:waterfall-api:1.16-R0.4-SNAPSHOT")
    compileOnly("com.gitlab.ruany:LiteBansAPI:0.3.4")
    compileOnly("net.luckperms:api:5.3")
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions{
        jvmTarget = JavaVersion.VERSION_16.toString()
    }
}

tasks.shadowJar {
    project.configurations.implementation.get().isCanBeResolved = true
    configurations = mutableListOf(project.configurations.implementation.get())
    minimize {}
}

tasks.processResources {
    expand("version" to project.version)
}

tasks.register("copyToServer"){
    dependsOn("shadowJar")

    doLast {
        copy {
            from("build/libs/DiscordHooks-" + project.version + "-all.jar")
            into("C:\\Users\\budi1\\Desktop\\Custom Plugins\\01-proxy\\plugins")
        }
    }
}