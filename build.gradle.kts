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

@file:Suppress("DEPRECATION")

import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import kotlinx.validation.ApiValidationExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask

buildscript {
  repositories {
    mavenCentral()
    google()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    gradlePluginPortal()
    maven("https://dl.bintray.com/kotlin/kotlinx")
  }
  dependencies {
    classpath(libs.agp)
    classpath(libs.kotlin.gradle.plug)
    classpath(libs.kotlinx.atomicfu)
    classpath(libs.ktlint.gradle)
    classpath(libs.vanniktech.maven.publish)
  }
}

plugins {
  kotlin("jvm")
  id("com.github.ben-manes.versions") version "0.39.0"
  id("io.gitlab.arturbosch.detekt") version "1.21.0"
  id("com.rickbusarow.module-check") version "0.12.3"
  id("com.dorongold.task-tree") version "2.1.0"
  id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.11.1"
  base
  dokka
  knit
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
  val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
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

allprojects {
  apply(plugin = "org.jlleitschuh.gradle.ktlint")

  configure<KtlintExtension> {
    debug.set(false)
    // when updating to 0.46.0:
    // - Re-enable `experimental:type-parameter-list-spacing`
    // - remove 'experimental' from 'argument-list-wrapping'
    // - remove 'experimental' from 'no-empty-first-line-in-method-block'
    version.set("0.45.2")
    outputToConsole.set(true)
    enableExperimentalRules.set(true)
    disabledRules.set(
      setOf(
        "max-line-length", // manually formatting still does this, and KTLint will still wrap long chains when possible
        "filename", // same as Detekt's MatchingDeclarationName, but Detekt's version can be suppressed and this can't
        "experimental:argument-list-wrapping", // doesn't work half the time
        "experimental:no-empty-first-line-in-method-block", // code golf...
        // This can be re-enabled once 0.46.0 is released
        // https://github.com/pinterest/ktlint/issues/1435
        "experimental:type-parameter-list-spacing",
        // added in 0.46.0
        "experimental:function-signature"
      )
    )
  }
  tasks.withType<BaseKtLintCheckTask> {
    workerMaxHeapSize.set("512m")
  }
}

val sortDependencies by tasks.registering {

  description = "sort all dependencies in a gradle kts file"
  group = "refactor"

  doLast {
    sortDependencies()
  }
}
