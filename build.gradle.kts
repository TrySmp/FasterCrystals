plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.3.0"
    id("com.gradleup.shadow") version "9.1.0"
    `maven-publish`
}

group = "xyz.reknown.fastercrystals"
version = "2.0.1"
description = "Uses packets to manually break/place crystals"

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")

    compileOnly("com.github.retrooper:packetevents-spigot:2.9.0-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
}

publishing {
    repositories {
        maven {
            name = "allay"
            url = uri("https://repo.allay-studios.com/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.trysmp"
            artifactId = "fastercrystals"
            version = project.version.toString()
            from(components["java"])
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

bukkitPluginYaml {
    name = "FasterCrystals"
    main = "xyz.reknown.fastercrystals.FasterCrystals"
    version = project.version.toString()
    authors.add("Jyguy")
    apiVersion = "1.20.5"
    foliaSupported = true
    depend.addAll("packetevents")
    commands.register("fastercrystals") {
        usage = "/fastercrystals <on/off|toggle>"
    }
}
