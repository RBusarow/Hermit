package hermit.test.internal

import hermit.test.*

/**
 * Lazy instance which can be reset.  After a reset, the next access will create a new instance.
 *
 * Each time a new instance is created, it is registered with the [ResetManager] and will be reset with the next [ResetManager.resetAll].
 *
 * @sample samples.LazyResetsSample.lazyResetClassSample
 */
internal class LazyResetsImpl<out T : Any>(
  private val resetManager: ResetManager,
  private val valueFactory: () -> T
) : LazyResets<T> {

  private var lazyHolder: Lazy<T> = createLazy()

  override val value: T
    get() = lazyHolder.value

  override fun isInitialized(): Boolean = lazyHolder.isInitialized()

  private fun createLazy() = lazy {
    resetManager.register(this)
    valueFactory()
  }

  override fun reset() {
    lazyHolder = createLazy()
  }
}
