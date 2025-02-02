plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.iridium"
version = "3.2.8"
description = "IridiumSkyblock"

repositories {
    mavenCentral()
    maven {
        credentials {
            username = project.property("nexusUsername").toString()
            password = project.property("nexusPassword").toString()
        }
        url = uri("https://repo.soknight.me/repository/releases/")
    }
    maven("https://hub.spigotmc.org/nexus/content/repositories/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://ci.ender.zone/plugin/repository/everything/")
    maven("https://jitpack.io")
    maven("https://redempt.dev")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://hub.jeff-media.com/nexus/repository/jeff-media-public/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://nexus.iridiumdevelopment.net/repository/maven-releases/")
}

dependencies {
    // Dependencies that we want to shade in
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("com.iridium:iridiumcore:1.6.6")
    implementation("com.github.Redempt:Crunch:1.0")
    implementation("com.j256.ormlite:ormlite-core:6.1")
    implementation("com.j256.ormlite:ormlite-jdbc:6.1")

    // Other dependencies that are not required or already available at runtime
    compileOnly("org.projectlombok:lombok:1.18.22")
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
    compileOnly("net.ess3:EssentialsXSpawn:2.16.1")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.9.2")
    compileOnly("com.gc:AdvancedSpawners:1.2.6")
    compileOnly("dev.rosewood:rosestacker:1.4.2")
    compileOnly("com.github.OmerBenGera:WildStackerAPI:master")
    compileOnly("com.songoda:UltimateStacker:2.1.7")
    compileOnly("com.songoda:EpicSpawners:7.0.8")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.6")

    // Enable lombok annotation processing
    annotationProcessor("org.projectlombok:lombok:1.18.22")

    // Test dependencies
    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.18:1.24.1")
}

tasks {
    // "Replace" the build task with the shadowJar task (probably bad but who cares)
    jar {
        dependsOn("shadowJar")
        enabled = false
    }

    shadowJar {
        fun relocate(origin: String) = relocate(origin, "com.iridium.iridiumskyblock.dependencies${origin.substring(origin.lastIndexOf('.'))}")

        // Remove the archive classifier suffix
        archiveClassifier.set("")

        // Relocate dependencies
        relocate("com.j256.ormlite")

        // Remove unnecessary files from the jar
        minimize()
    }

    // Set UTF-8 as the encoding
    compileJava {
        options.encoding = "UTF-8"
    }

    // Process Placeholders for the plugin.yml
    processResources {
        filesMatching("**/plugin.yml") {
            expand(rootProject.project.properties)
        }

        // Always re-run this task
        outputs.upToDateWhen { false }
    }

    test {
        useJUnitPlatform()
    }

    compileJava {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
    }

    compileTestJava {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }
}

// Set the Java version and vendor
java {
    withSourcesJar()
    toolchain {
        vendor.set(JvmVendorSpec.ADOPTOPENJDK)
    }
}

// Maven publishing to SoKnight's Nexus repository
publishing {
    publications.create<MavenPublication>("mavenJava") {
        artifactId = "iridiumskyblock"

        // Using compiled JARs instead of new publication creating
        artifact(tasks["shadowJar"])
        artifact(tasks["sourcesJar"])
    }

    repositories {
        maven {
            name = "nexus"
            url = uri("https://repo.soknight.me/repository/releases/")
            credentials {
                username = project.property("nexusUsername").toString()
                password = project.property("nexusPassword").toString()
            }
        }
    }
}
