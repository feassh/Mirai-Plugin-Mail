plugins {
    val kotlinVersion = "1.4.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.0-M2"
}

group = "ceneax.pther.miraipluginmail"
version = "0.0.1"

repositories {
    maven("https://maven.aliyun.com/repository/public/")
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}