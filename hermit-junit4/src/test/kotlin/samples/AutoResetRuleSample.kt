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

import hermit.test.Hermit
import hermit.test.ResetManager
import hermit.test.junit.HermitRule
import hermit.test.resets
import io.kotest.matchers.shouldBe

class AutoResetRuleSample {
  @JvmField
  @Rule
  val rule = HermitRule()

  /** automatically has reset() called on it after each test */
  val someProperty by rule.resets { SomeProperty() }

  val resetManager = Hermit()

  /** you can also pass in your own [ResetManager] */
  @JvmField
  @Rule
  val explicitRule = HermitRule(resetManager)

  @Test
  fun `some property gets reset`() {
    someProperty shouldBe false
  }
}

class SomeProperty

annotation class Test

annotation class Rule
