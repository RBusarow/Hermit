/*
 * Copyright (C) 2021-2022 Rick Busarow
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

plugins {
  `kotlin-dsl`
}

buildscript {
  dependencies {
    classpath(libs.kotlin.gradle.pluginApi)
  }
}

repositories {
  mavenCentral()
  google()
  maven("https://plugins.gradle.org/m2/")
}

val kotlinVersion = libs.versions.kotlin.get()

dependencies {

  compileOnly(gradleApi())

  implementation(kotlin("gradle-plugin", version = kotlinVersion))
  implementation(kotlin("reflect", version = kotlinVersion))

  implementation(libs.dokka.gradle)
  implementation(libs.dropbox.dependencyGuard)
  implementation(libs.kotlin.compiler)
  implementation(libs.kotlin.reflect)
  implementation(libs.kotlin.stdlib.jdk8)
  implementation(libs.kotlinx.atomicfu.gradle.plugin)
  implementation(libs.rickBusarow.ktlint)
  implementation(libs.rickBusarow.ktrules)
  implementation(libs.square.anvil.gradle)
  implementation(libs.vanniktech.maven.publish)
}
