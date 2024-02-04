rootProject.name = "kotlin-spring-hexagonal-architecture"

include(
    "main",
    "domain"
)

// DRIVER
include(
    "rest"
)
project(":rest").projectDir = file("driver/rest")

// DRIVEN
include(
    "persistence"
)
project(":persistence").projectDir = file("driven/persistence")
