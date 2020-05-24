package samples

import hermit.test.junit.*
import hermit.test.mockk.*
import io.mockk.*

class SimpleTest : AutoReset by AutoReset() {

  val car: Car by resetsMockk {
    every { manufacturer } returns "Honda"
  }

}

class Car {

  val manufacturer = "Toyota"

  fun nunmberOfWheels() = 4
}
