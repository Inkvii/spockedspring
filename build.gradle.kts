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

    // Testing libraries mandatory for spock
    testImplementation(group = "org.codehaus.groovy", name = "groovy-all", version = "2.4.15")
    testImplementation(group = "org.spockframework", name = "spock-core", version = "1.1-groovy-2.4")

    // For springboot test to work add this dependency
    testImplementation("org.spockframework:spock-spring:1.1-groovy-2.4")
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
