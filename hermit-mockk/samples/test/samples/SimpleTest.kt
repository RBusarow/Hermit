package samples

import hermit.test.junit.*
import hermit.test.mockk.*
import io.kotest.assertions.throwables.*
import io.kotest.matchers.*
import io.mockk.*
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimpleTest : HermitJUnit5() {

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

  fun nunmberOfWheels() = 4
}
