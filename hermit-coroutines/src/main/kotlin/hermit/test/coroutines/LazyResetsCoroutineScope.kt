package hermit.test.coroutines

import dispatch.core.*
import dispatch.test.TestProvidedCoroutineScope
import hermit.test.LazyResets
import hermit.test.ResetManager
import hermit.test.Resets
import hermit.test.coroutines.internal.LazyResetsCoroutineScopeImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestCoroutineScope

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

public interface LazyResetsCoroutineScope<T : CoroutineScope> : LazyResets<T>

@ExperimentalCoroutinesApi
public fun <T : CoroutineScope> lazyResetsCoroutineScope(
  resetManager: ResetManager,
  scope: T
): LazyResetsCoroutineScope<T> = LazyResetsCoroutineScopeImpl(resetManager, scope)
