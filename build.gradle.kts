import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
}

group = "org.mahata"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

val jaxwsSourceDir by extra { "${buildDir}/generated/sources/jaxws" }

configurations {
    create("jaxws")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-web-services")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    add("jaxws", "com.sun.xml.ws:jaxws-tools:3.0.0")
    add("jaxws", "jakarta.xml.ws:jakarta.xml.ws-api:3.0.0")
    add("jaxws", "jakarta.xml.bind:jakarta.xml.bind-api:3.0.0")
    add("jaxws", "jakarta.activation:jakarta.activation-api:2.0.0")
    add("jaxws", "com.sun.xml.ws:jaxws-rt:3.0.0")
}

val wsimport by tasks.creating {
    description = "Generate classes from wsdl using wsimport"
    doLast {
        project.mkdir(jaxwsSourceDir)
        ant.withGroovyBuilder {
            "taskdef"("name" to "wsimport",
                "classname" to "com.sun.tools.ws.ant.WsImport",
                "classpath" to configurations["jaxws"].asPath)
            "wsimport"("keep" to true,
                "destdir" to jaxwsSourceDir,
                "extension" to "true",
                "verbose" to true,
                "wsdl" to "http://localhost:18081/ws/countries.wsdl", // Path to WSDL
                "xnocompile" to true,
                "package" to "org.mahata.kotlinsoapclientdemo.wsdl") {
                "xjcarg"("value" to "-XautoNameResolution")
            }
        }
    }
}

sourceSets {
    main {
        java {
            srcDir(jaxwsSourceDir)
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }

    dependsOn(wsimport)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
