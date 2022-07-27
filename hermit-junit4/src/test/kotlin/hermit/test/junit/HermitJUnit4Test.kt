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

package hermit.test.junit

import hermit.test.Hermit
import hermit.test.resets
import io.kotest.matchers.shouldBe
import org.junit.After
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HermitJUnit4Test : HermitJUnit4(HermitJUnit4Test) {

  companion object : Hermit() {
    var resetCount = 0
    val subject by resets { TestSubject() }
  }

  @After
  fun after() {
    resetCount++
  }

  @Test
  fun `test 1`() {
    resetCount shouldBe 0
    ++subject.count shouldBe 1
  }

  @Test
  fun `test 2 -- executes second but should have a fresh state`() {
    resetCount shouldBe 1
    subject.count shouldBe 0
  }

  class TestSubject {
    var count = 0
  }
}
