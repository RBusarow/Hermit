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
import hermit.test.mockk.resetsMockk
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.MockKException
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SimpleTest : HermitJUnit5() {
  val car: Car by resetsMockk()

  @Test
  @Order(1)
  fun `test-specific answers should be applied`() {
    every { car.manufacturer } returns "Tesla"

    car.manufacturer shouldBe "Tesla"
  }

  @Test
  @Order(2)
  fun `mocks are strict by default`() {
    shouldThrow<MockKException> { car.manufacturer shouldBe "Honda" }
  }

  @Test
  @Order(3)
  fun `recorded calls are reset for each test`() {
    verify(exactly = 0) { car.manufacturer }
  }
}

class Car {
  val manufacturer = "Toyota"

  @Suppress("FunctionOnlyReturningConstant")
  fun numberOfWheels() = 4
}
