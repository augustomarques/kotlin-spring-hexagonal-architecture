import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

val javaVersion: String by project
val reactorKotlinExtensionVersion: String by project
val logbackEncoderVersion: String by project
val logbackClassicVersion: String by project
val instancioVersion: String by project
val assertjVersion: String by project
val mockkVersion: String by project

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin")
        classpath("org.springframework.boot:spring-boot-gradle-plugin")
    }
}

plugins {
    // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
    val kotlinVersion = "1.9.22"

    // https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint
    val ktlintVersion = "11.5.0"

    // https://plugins.gradle.org/plugin/org.barfuin.gradle.jacocolog
    val jacocoLogVersion = "3.1.0"

    val springBootVersion = "3.2.2"
    val springDependencyManagementVersion = "1.1.4"

    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion

    id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
    id("org.barfuin.gradle.jacocolog") version jacocoLogVersion
    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version springDependencyManagementVersion
    jacoco
    id("java-test-fixtures")
}

allprojects {
    repositories {
        mavenCentral()
    }

    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.kapt")
        plugin("kotlin-allopen")
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("jacoco")
        plugin("java-test-fixtures")
    }

    dependencies {
        apply(plugin = "kotlin")
        apply(plugin = "org.springframework.boot")
        apply(plugin = "io.spring.dependency-management")

        dependencies {
            implementation(kotlin("stdlib-jdk8"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
            implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
            implementation("org.jetbrains.kotlin:kotlin-reflect")
            implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
            implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

            runtimeOnly("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion") {
                exclude("ch.qos.logback")
                implementation("ch.qos.logback:logback-classic:$logbackClassicVersion")
            }

            testImplementation(testFixtures(project(":main")))
            testImplementation("org.springframework.boot:spring-boot-starter-test")
            testImplementation("org.instancio:instancio-junit:$instancioVersion")
            testImplementation("io.mockk:mockk:$mockkVersion")

            testFixturesImplementation("org.instancio:instancio-junit:$instancioVersion")
            testFixturesImplementation("org.assertj:assertj-core:$assertjVersion")
            testFixturesImplementation("org.jetbrains.kotlin:kotlin-reflect")
            testFixturesImplementation("io.mockk:mockk:$mockkVersion")
        }
    }

    group = "br.com.amarques"
    version = "1.0.0"

    sourceSets {
        main { java.setSrcDirs(listOf("src/main/kotlin")) }
        test { java.setSrcDirs(listOf("src/test/kotlin")) }
    }

    kotlin {
        sourceSets {
            main { kotlin.setSrcDirs(listOf("src/main/kotlin")) }
            test { kotlin.setSrcDirs(listOf("src/test/kotlin")) }
        }
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }

    ktlint {
        android.set(false)
        debug.set(false)
        enableExperimentalRules.set(false)
        ignoreFailures.set(false)
        outputColorName.set("RED")
        outputToConsole.set(true)
        verbose.set(true)

        filter {
            exclude("**/build/**")
            include("**/kotlin/**")
        }
    }

    tasks {
        compileJava {
            options.encoding = "UTF-8"
        }

        compileKotlin {
            kotlinOptions {
                jvmTarget = javaVersion
            }
        }

        compileTestKotlin {
            kotlinOptions {
                jvmTarget = javaVersion
            }
        }

        test {
            useJUnitPlatform()

            testLogging {
                showStandardStreams = true
                events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.STARTED, TestLogEvent.SKIPPED)
                exceptionFormat = TestExceptionFormat.FULL
            }

            reports {
                html.apply {
                    outputLocation.set(unitTestReportingDirectory.dir("html").asFile)
                    required.set(true)
                }

                junitXml.apply {
                    outputLocation.set(unitTestReportingDirectory.dir("xml").asFile)
                    required.set(true)
                    isOutputPerTestCase = false
                    mergeReruns.set(true)
                }
            }

            finalizedBy(
                jacocoTestReport,
                jacocoTestCoverageVerification
            )
        }

        withType<org.jlleitschuh.gradle.ktlint.tasks.GenerateReportsTask> {
            reportsOutputDirectory.set(linterReportingDirectory)
        }
    }
}

tasks {
    build {
        setDependsOn(subprojects.map { it.getTasksByName("build", false) })
    }

    check {
        setDependsOn(subprojects.map { it.getTasksByName("check", false) })
        finalizedBy(jacocoAggregatedReport)
    }

    jacocoAggregatedReport {
        reports {
            csv.apply {
                outputLocation.set(coverageReportingDirectory.dir("csv").file("result.csv").asFile)
                required.set(false)
            }

            html.apply {
                outputLocation.set(unitTestReportingDirectory.dir("html").asFile)
                required.set(true)
            }

            xml.apply {
                outputLocation.set(coverageReportingDirectory.dir("xml").file("result.xml").asFile)
                required.set(true)
            }
        }
    }
}

val Project.reportingDirectory: Directory
    get() = layout.buildDirectory.dir("report").get()

val Project.linterReportingDirectory: Directory
    get() = reportingDirectory.dir("linter")

val Project.unitTestReportingDirectory: Directory
    get() = reportingDirectory.dir("test")

val Project.coverageReportingDirectory: Directory
    get() = reporting.baseDirectory.get().dir("coverage")
