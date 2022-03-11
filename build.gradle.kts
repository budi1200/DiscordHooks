plugins {
    idea
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    kotlin("kapt") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("net.kyori.blossom") version "1.3.0"
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions{
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

group = "si.budimir"
version = "1.6.0"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    kapt("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")

    implementation("org.spongepowered:configurate-hocon:4.1.2")
    implementation("org.spongepowered:configurate-extra-kotlin:4.1.2")

    compileOnly("com.gitlab.ruany:LiteBansAPI:0.3.4")
    compileOnly("net.luckperms:api:5.4")
}

tasks.shadowJar {
    // This makes it shadow only stuff with "implementation"
    project.configurations.implementation.get().isCanBeResolved = true
    configurations = mutableListOf(project.configurations.implementation.get()) as List<FileCollection>?
}

tasks.processResources {
    expand("version" to project.version)
}

blossom {
    val file = "src/main/kotlin/si/budimir/discordHooks/util/Constants.kt"
    mapOf(
        "PLUGIN_NAME" to rootProject.name,
        "PLUGIN_VERSION" to project.version
    ).forEach { (k, v) ->
        replaceToken("{$k}", v, file)
    }
}

tasks.register("copyToServer"){
    dependsOn("shadowJar")

    doLast {
        copy {
            from("build/libs/DiscordHooks-" + project.version + "-all.jar")
            into("../01-velocity-proxy/plugins/")
        }
    }
}