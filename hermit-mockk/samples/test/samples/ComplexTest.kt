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
