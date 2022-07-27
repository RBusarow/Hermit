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

@file:Suppress("TooManyFunctions")

import org.gradle.api.Project
import java.io.File

fun cleanDocs() {
  val root = File("docs")

  root.walkTopDown()
    .forEach {
      if (it != root) {
        it.deleteRecursively()
      }
    }
}

fun Project.copyKdoc() {
  val root = File("$buildDir/dokka")

  val dirName = "$buildDir/dokka/${projectDir.name}"

  root.walkTopDown()
    .maxDepth(1)
    .filter { file ->

      file.isDirectory && file.path == dirName
    }
    .forEach { file ->

      file.copyRecursively(File("docs/kdoc/${projectDir.name}"))
    }
}

@Suppress("MagicNumber")
fun Project.copyReadMe() {
  val regex = "$projectDir/README.md".toRegex()

  projectDir.walkTopDown()
    // A depth of 3 allows for nested modules to introduce their README's.
    // This probably won't ever be necessary but it's like 10ms.
    .maxDepth(3)
    .filter { file ->

      file.path.matches(regex)
    }
    .forEach { file ->

      val newName = "${rootProject.rootDir}/docs/modules/${projectDir.name}.md"

      file.copyTo(File(newName))
    }
}

fun Project.copySite() {
  val root = File("$rootDir/site")

  root.walkTopDown()
    .maxDepth(1)
    .forEach { file ->

      if (!file.name.matches(".*mkdocs.yml".toRegex()) && file != root) {
        file.copyRecursively(File("$rootDir/docs"), true)
      }
    }
}

private data class DependencyMatcher(val fullCoordinate: String, val regex: Regex)

private fun String.replace(dependencyMatcher: DependencyMatcher): String =
  this.replace(dependencyMatcher.regex) { m1, m2, m3, m4 ->
    "$m1$m2${dependencyMatcher.fullCoordinate}$m3$m4"
  }

private fun String.replace(regex: Regex, block: (String) -> String): String =
  regex.replace(this) { match ->
    block(match.destructured.component1())
  }

private fun String.replace(regex: Regex, block: (String, String) -> String): String =
  regex.replace(this) { match ->
    block(match.destructured.component1(), match.destructured.component2())
  }

private fun String.replace(regex: Regex, block: (String, String, String) -> String): String =
  regex.replace(this) { match ->
    block(
      match.destructured.component1(),
      match.destructured.component2(),
      match.destructured.component3()
    )
  }

private fun String.replace(
  regex: Regex,
  block: (String, String, String, String) -> String
): String = regex.replace(this) { match ->
  block(
    match.destructured.component1(),
    match.destructured.component2(),
    match.destructured.component3(),
    match.destructured.component4()
  )
}
