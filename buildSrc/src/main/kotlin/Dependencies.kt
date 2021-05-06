/*
 * Copyright (C) 2020 Rick Busarow
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

object Plugins {

  const val atomicFu = "kotlinx-atomicfu"
  const val binaryCompatilibity = "binary-compatibility-validator"

  const val dokka = "org.jetbrains.dokka"
  const val detekt = "io.gitlab.arturbosch.detekt"
  const val knit = "kotlinx-knit"

  const val javaLibrary = "java-library"

  const val ktLint = "org.jlleitschuh.gradle.ktlint"
  const val kotlinter = "org.jmailen.kotlinter"

  const val mavenPublish = "com.vanniktech.maven.publish"
  const val benManes = "com.github.ben-manes.versions"
}

object Versions {
  const val ktlint = "0.35.0"
  const val dokka = "1.4.32"
  const val knit = "0.2.3"
  const val detekt = "1.16.0"

  const val kotlinter = "3.4.0"
  const val binaryCompatibility = "0.5.0"
  const val benManes = "0.38.0"
  const val androidTools = "4.2.0"
  const val kotlin = "1.5.0"
  const val mavenPublish = "0.13.0"
}

object BuildPlugins {

  const val gradleMavenPublish =
    "com.vanniktech:gradle-maven-publish-plugin:${Versions.mavenPublish}"
  const val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.dokka}"
  const val knit = "org.jetbrains.kotlinx:kotlinx-knit:${Versions.knit}"

  const val atomicFu = "org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.16.1"
  const val binaryCompatibility =
    "org.jetbrains.kotlinx:binary-compatibility-validator:${Versions.binaryCompatibility}"

  const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidTools}"
  const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
  const val benManesVersions = "com.github.ben-manes:gradle-versions-plugin:${Versions.benManes}"

  const val kotlinter = "org.jmailen.gradle:kotlinter-gradle:${Versions.kotlinter}"
}

object Libs {

  object JUnit {
    const val jUnit4 = "junit:junit:4.13.2"

    private const val version = "5.7.1"

    const val core = "org.junit.jupiter:junit-jupiter:$version"
    const val api = "org.junit.jupiter:junit-jupiter-api:$version"
    const val params = "org.junit.jupiter:junit-jupiter-params:$version"
    const val runtime = "org.junit.jupiter:junit-jupiter-engine:$version"
    const val vintage = "org.junit.vintage:junit-vintage-engine:$version"
  }

  object Kotlin {
    private const val version = "1.5.0"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
    const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
    const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    const val test = "org.jetbrains.kotlin:kotlin-test:$version"
    const val testCommon = "org.jetbrains.kotlin:kotlin-test-common:$version"
  }

  object Kotlinx {

    object Coroutines {
      private const val version = "1.4.2"
      const val common = "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$version"
      const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
      const val jdk8 = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$version"
      const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
      const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Knit {
      const val test = "org.jetbrains.kotlinx:kotlinx-knit-test:${Versions.knit}"
    }
  }

  object RickBusarow {

    object Dispatch {

      private const val version = "1.0.0-beta08"

      const val core = "com.rickbusarow.dispatch:dispatch-core:$version"
      const val detekt = "com.rickbusarow.dispatch:dispatch-detekt:$version"
      const val espresso = "com.rickbusarow.dispatch:dispatch-android-espresso:$version"
      const val lifecycle = "com.rickbusarow.dispatch:dispatch-android-lifecycle:$version"
      const val lifecycleExtensions =
        "com.rickbusarow.dispatch:dispatch-android-lifecycle-extensions:$version"
      const val viewModel = "com.rickbusarow.dispatch:dispatch-android-viewmodel:$version"

      object Test {
        const val core = "com.rickbusarow.dispatch:dispatch-test:$version"
        const val jUnit4 = "com.rickbusarow.dispatch:dispatch-test-junit4:$version"
        const val jUnit5 = "com.rickbusarow.dispatch:dispatch-test-junit5:$version"
      }
    }

    object Hermit {
      private const val version = "0.9.2"
      const val core = "com.rickbusarow.hermit:hermit-core:$version"
      const val coroutines = "com.rickbusarow.hermit:hermit-coroutines:$version"
      const val junit4 = "com.rickbusarow.hermit:hermit-junit4:$version"
      const val junit5 = "com.rickbusarow.hermit:hermit-junit5:$version"
      const val mockk = "com.rickbusarow.hermit:hermit-mockk:$version"
    }
  }
}
