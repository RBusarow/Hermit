package hermit.test.mockk

import hermit.test.*
import io.mockk.*
import kotlin.reflect.*

/**
 * Lazy instance which can be reset.  After a reset, the next access will create a new instance.
 *
 * Each time a new instance is created, it is registered with the [ResetManager] and will be reset with the next [ResetManager.resetAll].
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
