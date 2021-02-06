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

plugins {
  id(Plugins.dokka)
  id(Plugins.javaLibrary)
  id(Plugins.kotlin)
  id(Plugins.mavenPublish)
}

dependencies {

  api(project(":hermit-core"))

  implementation(Libs.JUnit.api)

  implementation(Libs.Kotlin.reflect)
  implementation(Libs.Kotlin.stdlib)

  implementation(Libs.Kotlinx.Coroutines.core)
  implementation(Libs.Kotlinx.Coroutines.test)

  implementation(Libs.MockK.core)

  implementation(Libs.RickBusarow.Dispatch.core)
  implementation(Libs.RickBusarow.Dispatch.Test.core)

  runtimeOnly(Libs.JUnit.runtime)

  testImplementation(project(":hermit-junit5"))

  testImplementation(Libs.Kotest.assertions)
  testImplementation(Libs.Kotest.properties)
  testImplementation(Libs.Kotest.runner)

  testImplementation(Libs.Kotlin.test)
  testImplementation(Libs.Kotlin.testCommon)
}