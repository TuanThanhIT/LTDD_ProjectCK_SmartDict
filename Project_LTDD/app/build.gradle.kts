plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.project_ltdd"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.project_ltdd"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures{
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }


}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.logging.interceptor)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("me.relex:circleindicator:2.1.6")
    implementation("com.google.mlkit:text-recognition:16.0.0")
    implementation("com.github.bumptech.glide:glide:4.13.2") // Kiểm tra phiên bản mới nhất của Glide
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.0")
    implementation("com.google.mlkit:translate:17.0.2")
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
	implementation(libs.lottie)
	implementation ("io.github.chaosleung:pinview:1.4.4")
    implementation(libs.pinview)
}