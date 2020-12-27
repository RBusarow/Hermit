package samples

import hermit.test.junit.*
import hermit.test.mockk.*
import io.kotest.matchers.*
import io.mockk.*
import org.junit.jupiter.api.*

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

    car.nunmberOfWheels() shouldBe 0 // relaxed return value
  }
}
