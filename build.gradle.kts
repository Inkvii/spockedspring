import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    war
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.spring") version "1.4.32"
    kotlin("plugin.jpa") version "1.4.32"
    id("groovy")
}

group = "verzich"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    // validation must be included in springboot, otherwise it wont work
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.h2database:h2")

    //Kotlin libraries
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Implementation of spring doc
    implementation("org.springdoc:springdoc-openapi-ui:1.5.2")

    // Helper implementation that improves documentation (applies JSR-303 Bean Validation and kotlin specific constraints to document)
    implementation("org.springdoc:springdoc-openapi-kotlin:1.3.9")
    // Dependency that assures it to be runnable on non-embedded tomcat
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Kotest
    val kotestVersion = "4.5.0"
    testImplementation("io.kotest:kotest-framework-engine:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    // Kotest extension for using springboottest
    testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
    // Kotest extension for using AbstractProjectConfig which declares kotest configuration globally
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.0.0")

    // Mokk for mocking
    testImplementation("io.mockk:mockk:1.11.0")

    // Optional kotest libraries
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
}
allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
tasks.withType<Test> {
    useJUnitPlatform()
}
