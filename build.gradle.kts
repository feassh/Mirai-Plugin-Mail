plugins {
    val kotlinVersion = "1.4.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.0-M2"
}

group = "ceneax.other.miraipluginmail"
version = "0.0.1"

repositories {
    maven("https://maven.aliyun.com/repository/public/")
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies {
    implementation("org.subethamail", "subethasmtp", "3.1.7")
    implementation("org.jsoup", "jsoup", "1.13.1")
}