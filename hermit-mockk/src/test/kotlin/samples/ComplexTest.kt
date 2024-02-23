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

import hermit.test.junit.HermitJUnit5
import hermit.test.mockk.ResetsMockK
import hermit.test.mockk.resetsMockk
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ComplexTest : HermitJUnit5() {
  val car: Car by resetsMockk(
    name = "Civic",
    relaxed = true,
    clearPolicy = ResetsMockK.ClearPolicy(recordedCalls = false)
  ) {
    every { manufacturer } returns "Honda"
  }

  @Test
  @Order(1)
  fun `test-specific answers should be applied`() {
    every { car.manufacturer } returns "Tesla"

    car.manufacturer shouldBe "Tesla"
  }

  @Test
  @Order(2)
  fun `answers from init block should be applied by default`() {
    car.manufacturer shouldBe "Honda"
  }

  @Test
  @Order(3)
  fun `recorded calls should not be reset for each test`() {
    verify(exactly = 2) { car.manufacturer }

    car.numberOfWheels() shouldBe 0 // relaxed return value
  }
}
