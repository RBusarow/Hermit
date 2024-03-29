/*
 * Copyright (C) 2022 Rick Busarow
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
@file:Suppress("UndocumentedPublicProperty", "UnstableApiUsage")

import com.vanniktech.maven.publish.GradlePlugin
import com.vanniktech.maven.publish.JavadocJar.Dokka
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost.Companion.DEFAULT
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.api.tasks.bundling.Jar
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.gradle.plugin.devel.PluginDeclaration
import org.gradle.plugins.signing.Sign
import org.jetbrains.dokka.gradle.AbstractDokkaLeafTask
import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

plugins {
  id("com.vanniktech.maven.publish.base")
  id("dokka")
  id("dependency-guard")
}

@Suppress("UnnecessaryAbstractClass")
abstract class HermitPublishingExtension {
  abstract val artifactId: Property<String>
}

val settings = extensions.create<HermitPublishingExtension>("hermitPublishing")

version = VERSION_NAME

var skipDokka = false

configure<MavenPublishBaseExtension> {
  publishToMavenCentral(DEFAULT)
  signAllPublications()
  pom {
    description.set("Resettable Lazy delegates which automatically reset with a test lifecycle")
    url.set(SOURCE_WEBSITE)
    licenses {
      license {
        name.set("The Apache Software License, Version 2.0")
        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
        distribution.set("repo")
      }
    }
    scm {
      url.set("$SOURCE_WEBSITE/")
      connection.set("scm:git:git://github.com/rbusarow/Hermit.git")
      developerConnection.set("scm:git:ssh://git@github.com/rbusarow/Hermit.git")
    }
    developers {
      developer {
        id.set("rbusarow")
        name.set("Rick Busarow")
      }
    }
  }

  when {
    pluginManager.hasPlugin("java-gradle-plugin") -> {
      configure(GradlePlugin(javadocJar = Dokka(taskName = "dokkaHtml"), sourcesJar = true))
    }

    pluginManager.hasPlugin("com.android.library") -> {
      configure(AndroidSingleVariantLibrary(sourcesJar = true))
    }

    else -> {
      configure(KotlinJvm(javadocJar = Dokka(taskName = "dokkaHtml"), sourcesJar = true))
    }
  }
}

afterEvaluate {
  project.configure<PublishingExtension> {
    publications
      .filterIsInstance<MavenPublication>()
      .forEach {
        it.groupId = GROUP
        it.artifactId = settings.artifactId.get()
      }
  }
  project.configure<MavenPublishBaseExtension> {
    pom {
      name.set(settings.artifactId)
    }
  }
}

tasks.register("checkVersionIsSnapshot") {
  doLast {
    val expected = "-SNAPSHOT"
    require(VERSION_NAME.endsWith(expected)) {
      "The project's version name must be suffixed with `$expected` when checked in" +
        " to the main branch, but instead it's `$VERSION_NAME`."
    }
  }
}

tasks.withType(PublishToMavenRepository::class.java).configureEach {
  notCompatibleWithConfigurationCache("See https://github.com/gradle/gradle/issues/13468")
}

tasks.withType(Jar::class.java).configureEach {
  notCompatibleWithConfigurationCache("")
}
tasks.withType(Sign::class.java).configureEach {
  notCompatibleWithConfigurationCache("")
  // skip signing for -LOCAL and -SNAPSHOT publishing
  onlyIf {
    !VERSION_NAME.endsWith("SNAPSHOT") && !VERSION_NAME.endsWith("LOCAL")
  }
}
tasks.matching { it.name == "javaDocReleaseGeneration" }.configureEach {
  onlyIf { !skipDokka }
}
tasks.withType(AbstractDokkaLeafTask::class.java).configureEach {
  onlyIf { !skipDokka }
}

// Integration tests require `publishToMavenLocal`, but they definitely don't need Dokka output,
// and generating kdoc for everything takes forever -- especially on a GitHub Actions server.
// So for integration tests, skip Dokka tasks.
val publishToMavenLocalNoDokka = tasks.register("publishToMavenLocalNoDokka") {

  doFirst { skipDokka = true }

  finalizedBy(rootProject.tasks.matching { it.name == "publishToMavenLocal" })
}

tasks.matching { it.name == "publishToMavenLocal" }.all {
  mustRunAfter(publishToMavenLocalNoDokka)
}

inline fun <reified T : Any> propertyDelegate(name: String): ReadWriteProperty<Any, T> {
  return object : ReadWriteProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
      return project.property(name) as T
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
      project.setProperty(name, value)
    }
  }
}
