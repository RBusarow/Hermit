package hermit.test.junit

import hermit.test.*
import org.junit.*
import hermit.test.resets as resetsExtension

abstract class HermitJUnit4(
  private val resetManager: ResetManager
) : ResetManager by resetManager {

  /**
   * @see ResetManager.resetAll
   */
  @After
  override fun resetAll() {
    resetManager.resetAll()
  }

  /**
   * Convenience function which delegates to [ResetManager.resets][resetsExtension] without needing the import.
   *
   * @see resetsExtension
   */
  inline fun <reified T : Any> resets(
    noinline valueFactory: () -> T = {
      val clazz = T::class

      try {
        clazz.java.getDeclaredConstructor().newInstance()
      } catch (illegal: IllegalAccessException) {
        val obj = clazz.objectInstance

        if (obj != null && obj is Resets) {
          obj
        } else {
          throw LazyResetDelegateObjectException(clazz)
        }
      } catch (abstract: InstantiationException) {
        when {
          clazz.java.isInterface -> throw LazyResetDelegateInterfaceException(clazz)
          clazz.isAbstract -> throw LazyResetDelegateAbstractException(clazz)
          else -> throw LazyResetDelegateNonDefaultConstructorException(clazz)
        }
      }
    }
  ) = resetsExtension(valueFactory)
}
