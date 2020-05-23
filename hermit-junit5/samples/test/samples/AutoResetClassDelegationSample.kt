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

import hermit.test.*
import hermit.test.junit.*
import io.kotest.matchers.*
import org.junit.jupiter.api.*

/**
 * The JUnit 5 extension is automatically applied,
 * and dependencies are provided via `by AutoReset()`.
 *
 * The test lifecycle is automatically managed.
 */
class AutoResetClassDelegationSample : AutoReset by AutoReset() {

  /**
   * Automatically reset between each test, including nested class tests.
   */
  val someDao: SomeDao by resets { SomeDao() }

  @Test
  fun `test one`() {

    val thing = Thing()

    someDao.insertThing(thing)

    someDao.getAllThings() shouldBe listOf(thing)
  }

  @Test
  fun `test two`() {

    // This test passes because someDao has been reset and is a different instance
    someDao.getAllThings() shouldBe listOf()
  }
}
