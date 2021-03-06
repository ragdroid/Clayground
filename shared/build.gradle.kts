import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.util.Properties
import java.io.FileInputStream
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.codingfeline.buildkonfig")
    id("com.android.library")
    id("com.chromaticnoise.multiplatform-swiftpackage") version "2.0.3"
}

//Workaround for issue https://youtrack.jetbrains.com/issue/KT-43944
android {
    configurations {
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
}

kotlin {

    android()
    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }
    macosX64("macOS") {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(DI.koinCore)
                implementation(Ktor.core)
                implementation(Ktor.json)
                implementation(Ktor.logging)
                implementation(Ktor.serialization)
                implementation(Serialization.kotlinx)
                implementation(Coroutines.coreNative){
                    version {
                        strictly(Coroutines.Versions.coreNative)
                    }
                }
                implementation(Stately.common)
                api(Other.kermit)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(DI.koinTest)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Ktor.android)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(Testing.junit)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Ktor.ios)
            }
        }

        val iosTest by getting

        val macOSMain by getting {
            dependencies {
                implementation(Ktor.mac)
            }
        }
    }
}

android {
    compileSdkVersion(AndroidSdk.compileSdk)
    defaultConfig {
        minSdkVersion(AndroidSdk.minSdk)
        targetSdkVersion(AndroidSdk.targetSdk)
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework =
        kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "framework")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)

buildkonfig {
    val properties = Properties()
    val configFile = file("config.properties")
    if (configFile.exists()) {
        properties.load(FileInputStream(configFile))
    }
    packageName = "com.ragdroid.clayground.shared"
    defaultConfigs {
        buildConfigField( STRING, "TMDB_API_TOKEN", properties["tmdb_api_token"] as String)
    }
}

multiplatformSwiftPackage {
    packageName("shared")
    swiftToolsVersion("5.3")
    targetPlatforms {
        iOS { v("13") }
        macOS { v("10") }
    }
}
