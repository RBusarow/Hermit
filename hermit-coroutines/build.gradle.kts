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
  id("javaLibrary")
  id("published")
}

hermitPublishing {
  artifactId.set("hermit-coroutines")
}

dependencies {

  api(projects.hermitCore)

  implementation(libs.dispatch.core)
  implementation(libs.dispatch.test.core)
  implementation(libs.junit.api)
  implementation(libs.kotlin.reflect)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.kotlinx.coroutines.test)
  implementation(libs.mockk)

  runtimeOnly(libs.junit.engine)

  testImplementation(libs.bundles.kotest)
  testImplementation(libs.kotlin.test.common)
  testImplementation(libs.kotlin.test.core)

  testImplementation(projects.hermitJunit5)
}
