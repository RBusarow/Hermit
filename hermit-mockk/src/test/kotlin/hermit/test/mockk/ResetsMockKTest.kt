package hermit.test.mockk

import hermit.test.Hermit
import hermit.test.ResetManager
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.freeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class ResetsMockKTest : FreeSpec({

  include(
    resetMockkTests(name = "ResetMockks class", resetManager = Hermit()) { manager, factory ->
      ResetsMockK(manager, mockk(), ResetsMockK.ClearPolicy(), factory)
    }
  )
  include(
    resetMockkTests(name = "delegate", resetManager = Hermit()) { manager, factory ->
      manager.resetsMockk(block = factory)
    }
  )
}) {
  override fun isolationMode(): IsolationMode? = IsolationMode.InstancePerLeaf
}

private inline fun resetMockkTests(
  name: String,
  resetManager: ResetManager,
  crossinline carFactory: (ResetManager, Car.() -> Unit) -> Lazy<Car>
) = freeSpec {
  name - {
    "Mock should behave according to answers block" - {
      val car by carFactory.invoke(resetManager) {
        every { manufacturer } returns "Ford"
        every { numberOfWheels() } returns 6
      }

      car.manufacturer shouldBe "Ford"
      car.numberOfWheels() shouldBe 6

      resetManager.resetAll()

      car.manufacturer shouldBe "Ford"
      car.numberOfWheels() shouldBe 6
    }

    "answers should be reset when ResetManager is reset" - {
      val car by carFactory.invoke(resetManager) {
        every { manufacturer } returns "Ford"
        every { numberOfWheels() } returns 6
      }

      every { car.numberOfWheels() } returns 7

      car.numberOfWheels() shouldBe 7

      resetManager.resetAll()

      car.numberOfWheels() shouldBe 6
    }

    "recorded calls should be reset when ResetManager is reset" - {
      val car by carFactory.invoke(resetManager) {
        every { manufacturer } returns "Ford"
        every { numberOfWheels() } returns 6
      }

      every { car.numberOfWheels() } returns 7

      car.numberOfWheels() shouldBe 7

      verify(exactly = 1) { car.numberOfWheels() }

      resetManager.resetAll()

      verify(exactly = 0) { car.numberOfWheels() }
    }
  }
}

class Car {

  val manufacturer = "Toyota"

  @Suppress("FunctionOnlyReturningConstant")
  fun numberOfWheels() = 4
}
