package hermit.test.coroutines

import dispatch.core.*
import dispatch.test.*
import hermit.test.*
import hermit.test.coroutines.internal.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*

/**
 * Binds a single instance of [CoroutineScope] to a [ResetManager].
 *
 * The same instance is re-used throughout the lifetime of the [LazyResetsCoroutineScope],
 * since its state is entirely reset in [reset][Resets.reset].
 *
 * ### reset behavior
 *
 * If the [scope] implements [TestCoroutineScope], [cleanupTestCoroutines][TestCoroutineScope.cleanupTestCoroutines] is called.
 *
 * If the [scope] does not implement [TestCoroutineScope] but its [coroutineContext][kotlin.coroutines.CoroutineContext] contains a [Job],
 * then [Job.cancelChildren] is called.
 *
 * If the [scope] is not a [TestCoroutineScope] and does not have a [Job], then [reset][Resets.reset] has no effect.
 *
 * @sample samples.ResetsScopeSample
 */
@ExperimentalCoroutinesApi
public inline fun <reified T : CoroutineScope> ResetManager.resetsScope(
  scope: T = when (T::class) {
    TestProvidedCoroutineScope::class -> TestProvidedCoroutineScope()
    TestCoroutineScope::class -> TestCoroutineScope()
    DefaultCoroutineScope::class -> DefaultCoroutineScope()
    IOCoroutineScope::class -> IOCoroutineScope()
    MainCoroutineScope::class -> MainCoroutineScope()
    MainImmediateCoroutineScope::class -> MainImmediateCoroutineScope()
    UnconfinedCoroutineScope::class -> UnconfinedCoroutineScope()
    else -> CoroutineScope(Job())
  } as T
): LazyResetsCoroutineScope<T> = lazyResetsCoroutineScope(this, scope)

interface LazyResetsCoroutineScope<T : CoroutineScope> : LazyResets<T>

@ExperimentalCoroutinesApi
fun <T : CoroutineScope> lazyResetsCoroutineScope(
  resetManager: ResetManager,
  scope: T
): LazyResetsCoroutineScope<T> = LazyResetsCoroutineScopeImpl(resetManager, scope)
