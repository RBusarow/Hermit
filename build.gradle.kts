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

import com.rickbusarow.ktlint.KtLintTask
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import kotlinx.validation.ApiValidationExtension
import kotlinx.validation.KotlinApiBuildTask
import kotlinx.validation.KotlinApiCompareTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.text.RegexOption.MULTILINE

buildscript {
  dependencies {
    classpath(libs.kotlin.gradle.plug)
  }
}

plugins {
  alias(libs.plugins.benManes)
  alias(libs.plugins.detekt)
  alias(libs.plugins.moduleCheck)
  alias(libs.plugins.kotlinx.binaryCompatibility)
  alias(libs.plugins.taskTree)
  base
  id("dokka")
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

  tasks.withType<Test>().configureEach {
    useJUnitPlatform()
  }
}

tasks.withType<DetektCreateBaselineTask>().configureEach {

  setSource(files(rootDir))

  include("**/*.kt", "**/*.kts")
  exclude("**/resources/**", "**/build/**", "**/src/test/java**")

  // Target version of the generated JVM bytecode. It is used for type resolution.
  this.jvmTarget = "1.8"
}

val detektLibraries = libs.detekt.rules.libraries.get()
val detektId = libs.plugins.detekt.get().pluginId

tasks.register("detektReportMerge", ReportMergeTask::class.java) {

  output.set(buildDir.resolve("reports/detekt/merged.sarif"))

  input.from(allprojects.map { it.tasks.withType(Detekt::class.java).map { it.sarifReportFile } })
}

allprojects {
  val target = this

  apply(plugin = detektId)

  detekt {

    parallel = true
    config.from(files("$rootDir/detekt/detekt-config.yml"))
  }

  tasks.withType<Detekt>().configureEach {

    reports {
      xml.required.set(true)
      html.required.set(true)
      txt.required.set(false)
      sarif.required.set(true)
    }
    config.from(files("$rootDir/detekt/detekt-config.yml"))

    setSource(files(projectDir))

    mustRunAfter(tasks.matching { it.name == "transformAtomicfuClasses" })

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

  target.dependencies {
    "detektPlugins"(detektLibraries)
  }

  if (target != target.rootProject) {
    target.tasks.register("detektAll", Detekt::class.java) {
      description = "runs the standard PSI Detekt as well as all type resolution tasks"
      dependsOn(
        target.tasks.withType(Detekt::class.java)
          .matching { it != this@register }
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
val ktrules = libs.rickBusarow.ktrules.get()

allprojects {
  apply(plugin = "com.rickbusarow.ktlint")

  val target = this@allprojects

  target.dependencies {
    "ktlint"(ktrules)
  }

  target.tasks.withType(KtLintTask::class.java).configureEach {
    dependsOn(":updateEditorConfigVersion")
    mustRunAfter(
      target.tasks.matching { it.name == "apiDump" },
      target.tasks.matching { it.name == "dependencyGuard" },
      target.tasks.matching { it.name == "dependencyGuardBaseline" },
      target.tasks.withType(KotlinApiBuildTask::class.java),
      target.tasks.withType(KotlinApiCompareTask::class.java)
    )
  }
}

allprojects {
  tasks.matching { it.name == "apiBuild" }.configureEach {
    dependsOn(tasks.matching { it.name == "transformAtomicfuClasses" })
  }
}

tasks.register("updateEditorConfigVersion") {

  val file = file(".editorconfig")

  doLast {
    val oldText = file.readText()

    val reg = """^(ktlint_kt-rules_project_version *?= *?)\S*$""".toRegex(MULTILINE)

    val newText = oldText.replace(reg, "$1$VERSION_NAME")

    if (newText != oldText) {
      file.writeText(newText)
    }
  }
}
