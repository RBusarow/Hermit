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

  api(project(":api"))

  implementation(Libs.JUnit.jUnit4)
  implementation(Libs.Kotlin.reflect)
  implementation(Libs.Kotlin.stdlib)

  testImplementation(Libs.KoTest.assertions)
  testImplementation(Libs.KoTest.properties)
  testImplementation(Libs.KoTest.runner)
  testImplementation(Libs.Kotlin.test)
  testImplementation(Libs.Kotlin.testCommon)
}
