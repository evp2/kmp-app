import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    iosArm64()
    iosSimulatorArm64()
    
    jvm()
    
    js {
        browser()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
        }
        commonMain.dependencies {
            // put Multiplatform dependencies here
            implementation("io.ktor:ktor-client-core:${libs.versions.ktor.get()}")
            implementation("io.ktor:ktor-client-content-negotiation:${libs.versions.ktor.get()}")
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.runtime)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(libs.sqlite.driver)
            implementation("io.ktor:ktor-client-java:${libs.versions.ktor.get()}")
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
            implementation(libs.android.driver)
            implementation("io.ktor:ktor-client-okhttp:${libs.versions.ktor.get()}")
        }
        iosMain.dependencies {
            implementation(libs.native.driver)
            implementation("io.ktor:ktor-client-darwin:${libs.versions.ktor.get()}")
        }
        jsMain.dependencies {
            implementation(libs.web.worker.driver)
            implementation("io.ktor:ktor-client-js:${libs.versions.ktor.get()}")
        }
        wasmJsMain.dependencies {
            implementation(kotlin("stdlib-wasm-js"))
            implementation(libs.web.worker.driver)
            implementation("io.ktor:ktor-client-core:${libs.versions.ktor.get()}")
        }
    }

}

android {
    namespace = "com.github.evp2.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.github.evp2.cache")
        }
    }
}