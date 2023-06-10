import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.1"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("com.palantir.docker") version "0.26.0"
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
    kotlin("plugin.jpa") version "1.4.21"
    kotlin("plugin.allopen") version "1.4.21"
}

group = "com"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

task<Copy>("unpack") {
    val bootJar = tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar")
    dependsOn(bootJar)
    from(zipTree(bootJar.outputs.files.singleFile))
    into("build/dependency")
}

docker {
    dependsOn(tasks.findByPath("build"))
    val archiveBaseName = tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar").archiveBaseName.get()
    name = "${project.group}/$archiveBaseName"
    setDockerfile(file("${projectDir}/src/docker"))
    files("build/libs/${project.name}-${version}.jar")
    buildArgs(mapOf("JAR_FILE" to "${project.name}-${version}.jar"))
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.2")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.2")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:2.4.1")
    implementation("org.springframework.boot:spring-boot-starter-mail:2.4.1")
    implementation("org.springframework:spring-context-support:5.2.8.RELEASE")
    implementation("org.apache.poi:poi:3.15")
    implementation("org.apache.poi:poi-ooxml:3.15")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.querydsl:querydsl-core:4.4.0")
    implementation("com.querydsl:querydsl-jpa:4.4.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}