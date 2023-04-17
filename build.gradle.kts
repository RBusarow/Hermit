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

import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import kotlinx.validation.ApiValidationExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.KotlinterExtension

plugins {
  kotlin("jvm")
  alias(libs.plugins.benManes)
  alias(libs.plugins.detekt)
  alias(libs.plugins.moduleCheck)
  alias(libs.plugins.kotlinx.binaryCompatibility)
  alias(libs.plugins.taskTree)
  alias(libs.plugins.kotlinter) apply false
  base
  dokka
}

moduleCheck {
  deleteUnused = true
  checks.sortDependencies = true
}

allprojects {

  repositories {
    mavenCentral()
    google()
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }
}

detekt {

  parallel = true
  config = files("$rootDir/detekt/detekt-config.yml")
}

tasks.withType<DetektCreateBaselineTask> {

  setSource(files(rootDir))

  include("**/*.kt", "**/*.kts")
  exclude("**/resources/**", "**/build/**", "**/src/test/java**")

  // Target version of the generated JVM bytecode. It is used for type resolution.
  this.jvmTarget = "1.8"
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {

  reports {
    xml.required.set(true)
    html.required.set(true)
    txt.required.set(false)
  }

  setSource(files(projectDir))

  include("**/*.kt", "**/*.kts")
  exclude(
    "**/resources/**",
    "**/dependencies/**",
    "**/build/**",
    "**/src/test/java**",
    "**/src/integrationTest/kotlin**",
    "**/src/test/kotlin**"
  )

  // Target version of the generated JVM bytecode. It is used for type resolution.
  this.jvmTarget = "1.8"
}

subprojects {
  tasks.withType<KotlinCompile>()
    .configureEach {

      kotlinOptions {
        allWarningsAsErrors = true

        jvmTarget = "1.8"
      }
    }
}

val cleanDocs by tasks.registering {

  description = "cleans /docs"
  group = "documentation"

  doLast {
    cleanDocs()
  }
}

extensions.configure<ApiValidationExtension> {

  /** Packages that are excluded from public API dumps even if they contain public API. */
  ignoredPackages = mutableSetOf("sample", "samples")

  /** Sub-projects that are excluded from API validation */
  ignoredProjects =
    mutableSetOf("samples")
}

fun isNonStable(version: String): Boolean {
  val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
  val regex = "^[0-9,.v-]+(-r)?$".toRegex()
  val isStable = stableKeyword || regex.matches(version)
  return isStable.not()
}

tasks.named(
  "dependencyUpdates",
  com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask::class.java
).configure {
  rejectVersionIf {
    isNonStable(candidate.version) && !isNonStable(currentVersion)
  }
}

val ktlintVersion = libs.versions.ktlint.lib.get()

allprojects {
  apply(plugin = "org.jmailen.kotlinter")

  extensions.configure(KotlinterExtension::class.java) {
    ignoreFailures = false
    reporters = arrayOf("checkstyle", "plain")
  }

  // dummy ktlint-gradle plugin task names which just delegate to the Kotlinter ones
  tasks.register("ktlintCheck") { dependsOn("lintKotlin") }
  tasks.register("ktlintFormat") { dependsOn("formatKotlin") }
}
