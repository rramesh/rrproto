import com.google.protobuf.gradle.*

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.41"
    // kapt plugin
    id("org.jetbrains.kotlin.kapt") version "1.3.50"
    // Protobuf
    id("com.google.protobuf") version "0.8.10"
    java
    application
}

repositories{
    jcenter()
    mavenCentral()
}

sourceSets {
    main {
        proto {
            srcDir("proto")
            srcDir("src/main/grpc")
        }
    }
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // Kotlin coroutines
    compile("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.3.2")

    // GRPC, Protobuf
//    compile("com.google.protobuf", "protobuf-java", "3.10.0")
    compile("io.grpc", "grpc-protobuf", "1.25.0")
    compile("io.grpc", "grpc-stub", "1.25.0")
//    compile("io.grpc", "grpc-netty-shaded", "1.25.0")
}

protobuf {
    protoc { artifact = "com.google.protobuf:protoc:3.10.0"}
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.25.0"
        }
        id("grpckotlin") {
            artifact = "io.rouz:grpc-kotlin-gen:0.1.1:jdk8@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckotlin")
            }
        }
    }
    generatedFilesBaseDir = "$projectDir/src"
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}