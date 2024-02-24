// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    //val kotlin_version = "1.6.21"
    val kotlin_version = "1.8.0"

    repositories {
        google()
        mavenCentral()

    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
        classpath("com.google.gms:google-services:4.4.1")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
    }
}

plugins {
    id("com.diffplug.spotless") version ("5.14.0")
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    id ("com.google.dagger.hilt.android") version "2.48" apply false
//    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
//    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
}

allprojects {

    repositories {
        google()
        mavenCentral()
    }

    apply {
        plugin("com.diffplug.spotless")
    }

    spotless {

        format("misc") {
            target("**/*.gradle', '**/*.md', '**/.gitignore")
            indentWithSpaces()
            trimTrailingWhitespace()
            endWithNewline()
        }

        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")
            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
            ktlint("0.41.0").userData(mapOf("disabled_rules" to "no-wildcard-imports"))
        }
    }
}

tasks.register(name = "type", type = Delete::class) {
    delete(rootProject.buildDir)
}
