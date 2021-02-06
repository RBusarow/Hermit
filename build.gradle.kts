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

import kotlinx.knit.*
import kotlinx.validation.*
import org.jetbrains.dokka.gradle.*
import org.jetbrains.kotlin.gradle.tasks.*
import java.net.*

buildscript {
  repositories {
    mavenCentral()
    google()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    jcenter()
    gradlePluginPortal()
    maven("https://dl.bintray.com/kotlin/kotlinx")
  }
  dependencies {
    classpath(BuildPlugins.androidGradlePlugin)
    classpath(BuildPlugins.atomicFu)
    classpath(BuildPlugins.benManesVersions)
    classpath(BuildPlugins.kotlinGradlePlugin)
    classpath(BuildPlugins.gradleMavenPublish)
    classpath(BuildPlugins.binaryCompatibility)
    classpath(BuildPlugins.dokka)
    classpath(BuildPlugins.knit)
    classpath(BuildPlugins.kotlinter)
  }
}

plugins {
  id(Plugins.benManes) version Versions.benManes
  id(Plugins.dokka) version Versions.dokka
  base
}

allprojects {

  repositories {
    mavenCentral()
    google()
    jcenter()
  }
}

tasks.dokkaHtmlMultiModule.configure {

  outputDirectory.set(buildDir.resolve("dokka"))

  // missing from 1.4.10  https://github.com/Kotlin/dokka/issues/1530
  // documentationFileName.set("README.md")
}

subprojects {

  tasks.withType<DokkaTask>().configureEach {

    dependsOn(allprojects.mapNotNull { it.tasks.findByName("assemble") })

    outputDirectory.set(buildDir.resolve("dokka"))

    dokkaSourceSets.configureEach {

      jdkVersion.set(8)
      reportUndocumented.set(true)
      skipEmptyPackages.set(true)
      noAndroidSdkLink.set(false)

      samples.from(files("samples"))

      if (File("$projectDir/README.md").exists()) {
        includes.from(files("README.md"))
      }

      sourceLink {

        val modulePath = this@subprojects.path.replace(":", "/").replaceFirst("/", "")

        // Unix based directory relative path to the root of the project (where you execute gradle respectively).
        localDirectory.set(file("src/main"))

        // URL showing where the source code can be accessed through the web browser
        remoteUrl.set(uri("https://github.com/RBusarow/Dispatch/blob/main/$modulePath/src/main").toURL())
        // Suffix which is used to append the line number to the URL. Use #L for GitHub
        remoteLineSuffix.set("#L")
      }
    }
  }
}

subprojects {

  // force update all transitive dependencies (prevents some library leaking an old version)
  configurations.all {
    resolutionStrategy {
      force(
        Libs.Kotlin.stdlib,
        Libs.Kotlin.reflect,
        // androidx is currently leaking coroutines 1.1.1 everywhere
        Libs.Kotlinx.Coroutines.android,
        Libs.Kotlinx.Coroutines.common,
        Libs.Kotlinx.Coroutines.core,
        Libs.Kotlinx.Coroutines.jdk8,
        Libs.Kotlinx.Coroutines.test
      )
    }
  }
}

subprojects {
  tasks.withType<KotlinCompile>()
    .configureEach {

      kotlinOptions {
        allWarningsAsErrors = true

        jvmTarget = "1.8"

        // https://youtrack.jetbrains.com/issue/KT-24946
//         freeCompilerArgs = listOf(
//             "-progressive",
//             "-Xskip-runtime-version-check",
//             "-Xdisable-default-scripting-plugin",
//             "-Xuse-experimental=kotlin.Experimental"
//         )
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

val copyRootFiles by tasks.registering {

  description = "copies documentation files from the project root into /docs"
  group = "documentation"

  dependsOn("cleanDocs")

  doLast {
    copySite()
    copyRootFiles()
  }
}

apply(plugin = Plugins.knit)

extensions.configure<KnitPluginExtension> {

  rootDir = File(".")
  moduleRoots = listOf(".")

  moduleDocs = "build/dokka"
  moduleMarkers = listOf("build.gradle", "build.gradle.kts")
  siteRoot = "https://rbusarow.github.io/Hermit"
}

// Build API docs for all modules with dokka before running Libs.Kotlinx.Knit
tasks.getByName("knitPrepare") {
  dependsOn(subprojects.mapNotNull { it.tasks.findByName("dokka") })
}

apply(plugin = Plugins.binaryCompatilibity)

extensions.configure<ApiValidationExtension> {

  /**
   * Packages that are excluded from public API dumps even if they
   * contain public API.
   */
  ignoredPackages = mutableSetOf("sample", "samples")

  /**
   * Sub-projects that are excluded from API validation
   */
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
  apply(plugin = Plugins.kotlinter)

  extensions.configure<org.jmailen.gradle.kotlinter.KotlinterExtension> {

    ignoreFailures = false
    reporters = kotlin.arrayOf("checkstyle", "plain")
    experimentalRules = true
    disabledRules = kotlin.arrayOf(
      "no-multi-spaces",
      "no-wildcard-imports",
      "max-line-length", // manually formatting still does this, and KTLint will still wrap long chains when possible
      "filename", // same as Detekt's MatchingDeclarationName, except Detekt's version can be suppressed and this can't
      "experimental:argument-list-wrapping" // doesn't work half the time
    )
  }
}
