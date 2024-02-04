import org.springframework.boot.gradle.tasks.bundling.BootJar

val springdocOpenApiVersion: String by project

tasks.named<BootJar>("bootJar") {
    enabled = false
}

dependencies {
    api(project(":domain"))

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocOpenApiVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}
