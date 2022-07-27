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

package samples

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

typealias Sample = Test

infix fun Any?.shouldPrint(
  expected: String
) = toString() shouldBe expected

infix fun List<Any?>.shouldPrint(
  expected: String
) = joinToString("\n") shouldBe expected

open class SampleTest {

  protected val output = mutableListOf<String>()

  @AfterEach
  fun afterEach() {
    output.clear()
  }

  protected fun println(message: Any?) {
    output.add("$message")
  }
}
