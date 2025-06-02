plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.github.funczz.ruby_calc.android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.github.funczz.ruby_calc.android"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {
    //JRuby
    implementation("org.jruby:jruby:1.7.27")

    //Math functions for BigDecimal https://github.com/eobermuhlner/big-math
    implementation("ch.obermuhlner:big-math:2.3.2")

    //ZIP - convenience methods https://github.com/zeroturnaround/zt-zip
    implementation("org.zeroturnaround:zt-zip:1.17")

    //RDBMS
    implementation("org.hsqldb:hsqldb:2.4.1")
    implementation("commons-dbcp:commons-dbcp:1.4")
    implementation("org.ktorm:ktorm-core:3.6.0")
    implementation("com.github.funczz:migration:0.5.0")

    //Etc.
    implementation("com.github.funczz:notifier:0.10.0")
    implementation("com.github.funczz:sam:0.2.0")

    //Robolectric
    testImplementation("org.robolectric:robolectric:4.13")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite-android:1.3.0-beta05")
    implementation("androidx.compose.material:material-icons-extended:1.6.8")
}
