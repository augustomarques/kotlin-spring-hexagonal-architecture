val wiremockVersion: String by project

plugins {
    // https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow
    val shadowVersion = "8.1.1"

    application

    id("com.github.johnrengelman.shadow") version shadowVersion
}

application {
    mainClass.set("br.com.amarques.MainKt")

    applicationDefaultJvmArgs = listOf(
        "-server",
        "-XX:+UseNUMA",
        "-XX:+UseParallelGC"
    )
}

tasks {
    shadowJar {
        archiveBaseName.set("app")
        archiveVersion.set("")
        archiveAppendix.set("")
        archiveClassifier.set("")
    }
    test {
        jvmArgs = listOf("-Duser.timezone=UTC")
    }
}

dependencies {
    listOf(
        ":domain",
        // DRIVEN
        ":persistence",
        // DRIVER
        ":rest"
    ).forEach {
        implementation(project(it))
        testFixturesImplementation(project(it))
    }

    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.wiremock:wiremock:$wiremockVersion")

    testFixturesImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testFixturesImplementation("org.wiremock:wiremock:$wiremockVersion")
}
