import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.cicd.seeroo_sample"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.cicd.seeroo_sample"
        minSdk = 24
        targetSdk = 36

        // --- Version Code from GitHub Actions ---
        val ciVersionCode = System.getenv("VERSION_CODE")?.toIntOrNull()
        val finalVersionCode = ciVersionCode ?: 1
        versionCode = finalVersionCode

        // --- Version Name Auto Generate ---
        versionName = "1.0.$finalVersionCode"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val envStoreFile = System.getenv("KEYSTORE_PATH")
            val envStorePassword = System.getenv("KEYSTORE_PASSWORD")
            val envKeyAlias = System.getenv("KEY_ALIAS")
            val envKeyPassword = System.getenv("KEY_PASSWORD")

            if (!envStoreFile.isNullOrEmpty()) {
                // CI (GitHub Actions)
                storeFile = file(envStoreFile)
                storePassword = envStorePassword
                keyAlias = envKeyAlias
                keyPassword = envKeyPassword
            } else {
                // Running locally
                val props = Properties()
                val propsFile = rootProject.file("key.properties")

                if (propsFile.exists()) {
                    props.load(propsFile.inputStream())
                    storeFile = file(props["storeFile"] as String)
                    storePassword = props["storePassword"] as String
                    keyAlias = props["keyAlias"] as String
                    keyPassword = props["keyPassword"] as String
                }
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}