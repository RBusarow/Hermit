package hermit.test.coroutines.internal

import hermit.test.*
import hermit.test.coroutines.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*

@ExperimentalCoroutinesApi
internal class LazyResetsCoroutineScopeImpl<T : CoroutineScope>(
  private val resetManager: ResetManager,
  private val scopeFactory: () -> T
) : LazyResetsCoroutineScope<T> {

  private var lazyHolder: Lazy<T> = createLazy()

  override val value: T
    get() = lazyHolder.value

  override fun isInitialized(): Boolean = lazyHolder.isInitialized()

  private fun createLazy() = lazy {
    resetManager.register(this)
    scopeFactory.invoke()
  }

  override fun reset() {
    val scope = if (lazyHolder.isInitialized()) {
      lazyHolder.value
    } else {
      null
    }
    if (scope is TestCoroutineScope) {
      scope.cleanupTestCoroutines()
    } else {
      scope?.coroutineContext
        ?.get(Job)
        ?.cancelChildren()
    }
    lazyHolder = createLazy()
  }
}
