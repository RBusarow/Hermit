package hermit.test.junit

import hermit.test.*
import org.junit.*

abstract class HermitJUnit4(
  resetManager: ResetManager
) : ResetManager by resetManager {

  @After
  fun _after() {
    resetAll()
  }

}
