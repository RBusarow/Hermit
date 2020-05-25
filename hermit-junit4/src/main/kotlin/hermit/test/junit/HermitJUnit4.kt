package hermit.test.junit

import hermit.test.*
import org.junit.*

abstract class HermitJUnit4(
  private val resetManager: ResetManager
) : ResetManager by resetManager {

  @After
  override fun resetAll() {
    resetManager.resetAll()
  }
}
