package hermit.test.coroutines.internal

import hermit.test.*
import hermit.test.coroutines.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*

@ExperimentalCoroutinesApi
internal class LazyResetsCoroutineScopeImpl<T : CoroutineScope>(
  private val resetManager: ResetManager,
  private val scope: T
) : LazyResetsCoroutineScope<T> {

  private var lazyHolder: Lazy<T> = createLazy()

  override val value: T
    get() = lazyHolder.value

  override fun isInitialized(): Boolean = lazyHolder.isInitialized()

  private fun createLazy() = lazy {
    resetManager.register(this)
    scope
  }

  override fun reset() {
    if (scope is TestCoroutineScope) {
      scope.cleanupTestCoroutines()
    } else {
      scope.coroutineContext[Job]?.cancelChildren()
    }
    lazyHolder = createLazy()
  }
}
