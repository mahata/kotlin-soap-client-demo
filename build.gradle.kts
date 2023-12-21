import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    id("org.jlleitschuh.gradle.ktlint") version "12.0.3"
}

group = "org.mahata"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

val jaxwsSourceDir by extra {
    layout.buildDirectory.dir("generated/sources/jaxws").get().asFile.absolutePath
}

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
    testImplementation("io.mockk:mockk:1.13.8")

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
            "taskdef"(
                "name" to "wsimport",
                "classname" to "com.sun.tools.ws.ant.WsImport",
                "classpath" to configurations["jaxws"].asPath,
            )
            "wsimport"(
                "keep" to true,
                "destdir" to jaxwsSourceDir,
                "extension" to "true",
                "verbose" to true,
                // Path to WSDL
                "wsdl" to "http://localhost:18081/ws/countries.wsdl",
                "xnocompile" to true,
                "package" to "org.mahata.kotlinsoapclientdemo.wsdl",
            ) {
                "xjcarg"("value" to "-XautoNameResolution")
            }
        }
    }
}

val lintOption: String =
    if (System.getenv("GITHUB_WORKFLOW") == null) {
        "ktlintFormat"
    } else {
        "ktlintCheck"
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

    dependsOn(wsimport, lintOption)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
}
