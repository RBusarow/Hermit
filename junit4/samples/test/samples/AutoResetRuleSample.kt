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

package samples

import hermit.*
import hermit.junit.*
import io.kotest.matchers.*

class AutoResetRuleSample {

  @JvmField
  @Rule
  val rule = AutoResetRule()

  /**
   * automatically has reset() called on it after each test
   */
  val someProperty by rule.resets { SomeProperty() }

  val resetManager = ResetManager()

  /**
   * you can also pass in your own [ResetManager]
   */
  @JvmField
  @Rule
  val explicitRule = AutoResetRule(resetManager)

  @Test
  fun `some property gets reset`() {

    someProperty shouldBe false

  }
}

class SomeProperty

annotation class Test
annotation class Rule
