plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.taufik.androidmachinelearning"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.taufik.androidmachinelearning"
        minSdk = 31
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
        mlModelBinding = true
    }
    androidResources {
        noCompress += "tflite"
    }
}

dependencies {

    // Core + KTX
    val coreKtxVersion = "1.12.0"
    val appCompatVersion = "1.6.1"
    val activityKtxVersion = "1.8.2"
    val fragmentKtxVersion = "1.6.2"
    implementation("androidx.appcompat:appcompat:$appCompatVersion")
    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("androidx.activity:activity-ktx:$activityKtxVersion")
    implementation("androidx.fragment:fragment-ktx:$fragmentKtxVersion")

    // UI
    val materialVersion = "1.11.0"
    val constraintLayoutVersion = "2.1.4"
    implementation("com.google.android.material:material:$materialVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")

    // Testing
    val jUnitVersion = "4.13.2"
    val jUnitExtVersion = "1.1.5"
    val espressoVersion = "3.5.1"
    testImplementation("junit:junit:$jUnitVersion")
    androidTestImplementation("androidx.test.ext:junit:$jUnitExtVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")

    // CameraX
    val cameraXVersion = "1.3.1"
    implementation("androidx.camera:camera-camera2:$cameraXVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraXVersion")
    implementation("androidx.camera:camera-view:$cameraXVersion")

    // Retrofit
    val retrofitVersion = "2.9.0"
    val loggingInterceptorVersion = "4.12.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$loggingInterceptorVersion")

    // Lifecycle
    val lifecycleVersion = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion") //untuk lifecycleScope
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    // MLKit
    val textRecognitionVersion = "16.0.0"
    val translateVersion = "17.0.2"
    val barcodeScanningVersion = "18.3.0"
    val visionVersion = "1.4.0-alpha04"
    implementation("com.google.mlkit:text-recognition:$textRecognitionVersion")
    implementation("com.google.mlkit:translate:$translateVersion")
    implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:$barcodeScanningVersion")
    implementation("androidx.camera:camera-mlkit-vision:$visionVersion")

    // TFLite
    val tfLiteTaskVisionVersion = "0.4.4"
    implementation("org.tensorflow:tensorflow-lite-task-vision:$tfLiteTaskVisionVersion")
    implementation("org.tensorflow:tensorflow-lite-support:$tfLiteTaskVisionVersion")
    implementation("org.tensorflow:tensorflow-lite-metadata:$tfLiteTaskVisionVersion")
    
    // TFLite + Google Play + Prediction
    val playServiceTFLiteVersion = "16.1.0"
    val taskVisionTFLiteServicesVersion = "0.4.2"
    val gmsPlayServicesTFLiteGPUVersion = "16.2.0"
    implementation("com.google.android.gms:play-services-tflite-support:$playServiceTFLiteVersion")
    implementation("org.tensorflow:tensorflow-lite-task-vision-play-services:$taskVisionTFLiteServicesVersion")
    implementation("com.google.android.gms:play-services-tflite-java:$playServiceTFLiteVersion")
    implementation("com.google.android.gms:play-services-tflite-gpu:$gmsPlayServicesTFLiteGPUVersion")

    // MediaPipe
    val mediaPipeVersion = "0.20230731"
    implementation("com.google.mediapipe:tasks-vision:$mediaPipeVersion")
    implementation("com.google.mediapipe:tasks-audio:$mediaPipeVersion")
    implementation("com.google.mediapipe:tasks-text:$mediaPipeVersion")

    // Generative AI
    val mlKitSmartReplyVersion = "16.0.0-beta1"
    implementation("com.google.android.gms:play-services-mlkit-smart-reply:$mlKitSmartReplyVersion")

    // Navigation library
    val navigationVersion = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")


    // GSON library
    val gsonVersion = "2.10"
    implementation("com.google.code.gson:gson:$gsonVersion")
}