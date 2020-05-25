package hermit.test.junit

import hermit.test.*
import org.junit.jupiter.api.*

abstract class HermitJUnit5(
  delegates: MutableCollection<Resets> = mutableListOf()
) : Hermit(delegates),
    AutoReset {

  @Suppress("FunctionName")
  @AfterEach
  private fun _afterEach() {
    resetAll()
  }
}
