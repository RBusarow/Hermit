package hermit.test.mockk

import hermit.test.*
import io.mockk.*
import kotlin.reflect.*

/**
 * Lazy MockK object for which [reset] will call [clearMocks].
 *
 * # Lifecycle
 *
 * ## On initialization
 * * creates a MockK instance
 * * *if [block] was specified* sets all answers from the `every { ... }` [block] argument
 *
 * ## On first access after reset
 * * registers the [mock] with the [resetManager]
 *
 * ## On repeated access
 * * returns the same [mock] instance and does nothing with [block]
 *
 * ## On reset
 * * calls [clearMocks(mock)][clearMocks] and eagerly invokes [block] again to reset the state to the initial one
 *
 * **Implementation Note**
 *
 * This behavior of eagerly invoking the answers [block] is atypical for this library.  It is necessary
 * because otherwise, it is likely that the first access of the [mock] instance would be inside an [every] block.
 * Attempting to invoke [every] while already inside [every] causes a [MockKException].
 *
 * @sample samples.LazyResetsSample.lazyResetClassSample
 */
public class ClearsMockK<out T : Any>(
  private val resetManager: ResetManager,
  private val mock: T,
  private val block: T.() -> Unit = {}
) : Lazy<T>,
    Resets {

  init {
    mock.block()
  }

  private var lazyHolder: Lazy<T> = createLazy()

  override val value: T
    get() = lazyHolder.value

  override fun isInitialized(): Boolean = lazyHolder.isInitialized()

  private fun createLazy() = lazy {
    resetManager.register(this)
    mock
  }

  override fun reset() {
    lazyHolder = createLazy()
    clearMocks(mock)
    mock.block()
  }
}

public inline fun <reified T : Any> ResetManager.clears(
  name: String? = null,
  relaxed: Boolean = false,
  vararg moreInterfaces: KClass<*>,
  relaxUnitFun: Boolean = false,
  noinline block: T.() -> Unit = {}
): ClearsMockK<T> = ClearsMockK(
  resetManager = this,
  mock = mockk(
    name = name,
    relaxed = relaxed,
    moreInterfaces = *moreInterfaces, relaxUnitFun = relaxUnitFun
  ),
  block = block
)
