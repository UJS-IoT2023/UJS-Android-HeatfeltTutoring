plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.spring") version "2.2.0"
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "2.2.0"
}
val springAiVersion by extra("1.1.1")

group = "cn.arorms.android.ht"
version = "0.0.1-SNAPSHOT"
description = "Server"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-websocket")


    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.ai:spring-ai-starter-model-openai")

    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    // Hibernate Jackson module for handling lazy loading in JSON serialization
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate6")

    runtimeOnly("org.postgresql:postgresql")

    // Aliyun OSS
    implementation("com.aliyun.oss:aliyun-sdk-oss:3.17.4")

    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    testRuntimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}
dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:$springAiVersion")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
