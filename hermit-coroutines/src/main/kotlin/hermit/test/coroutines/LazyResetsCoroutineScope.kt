/*
 * Copyright (C) 2021-2022 Rick Busarow
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hermit.test.coroutines

import dispatch.core.DefaultCoroutineScope
import dispatch.core.IOCoroutineScope
import dispatch.core.MainCoroutineScope
import dispatch.core.MainImmediateCoroutineScope
import dispatch.core.UnconfinedCoroutineScope
import dispatch.test.TestProvidedCoroutineScope
import hermit.test.LazyResets
import hermit.test.ResetManager
import hermit.test.Resets
import hermit.test.coroutines.internal.LazyResetsCoroutineScopeImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestScope

/**
 * Factory function for creating a [LazyResetsCoroutineScope]. This binds a single
 * instance of [CoroutineScope] to a [ResetManager][hermit.test.ResetManager].
 *
 * The factory function's created scope instance persists throughout the lifetime of
 * the LazyResetsCoroutineScope. The state is reset via the [Resets.reset] method.
 *
 * @param cleanUpTestCoroutines determines whether
 *   [cleanupTestCoroutines][TestCoroutineScope.cleanupTestCoroutines] should be
 *   invoked if the [CoroutineScope] is of type [TestCoroutineScope]. Defaults to true.
 * @param scopeFactory the factory function responsible for creating the
 *   [CoroutineScope] instance. If not provided, a default factory method is
 *   used, which creates a [CoroutineScope] based on the specified generic type.
 * @receiver [ResetManager] that manages the resetting of the [CoroutineScope] instance.
 * @return [LazyResetsCoroutineScope] instance that lazily provides and resets the [CoroutineScope].
 * @sample samples.ResetsScopeSample
 */
@ExperimentalCoroutinesApi
public inline fun <reified T : CoroutineScope> ResetManager.resetsScope(
  cleanUpTestCoroutines: Boolean = true,
  noinline scopeFactory: () -> T = {
    @Suppress("DEPRECATION")
    when (T::class) {
      TestProvidedCoroutineScope::class -> TestProvidedCoroutineScope()
      TestScope::class -> TestScope()
      TestCoroutineScope::class -> TestCoroutineScope()
      DefaultCoroutineScope::class -> DefaultCoroutineScope()
      IOCoroutineScope::class -> IOCoroutineScope()
      MainCoroutineScope::class -> MainCoroutineScope()
      MainImmediateCoroutineScope::class -> MainImmediateCoroutineScope()
      UnconfinedCoroutineScope::class -> UnconfinedCoroutineScope()
      else -> CoroutineScope(Job())
    } as T
  }
): LazyResetsCoroutineScope<T> = lazyResetsCoroutineScope(this, cleanUpTestCoroutines, scopeFactory)

/**
 * Interface extending from [LazyResets][hermit.test.LazyResets].
 * Provides the lazily instantiated [CoroutineScope] that can be reset.
 */
public interface LazyResetsCoroutineScope<T : CoroutineScope> : LazyResets<T>

/**
 * Function for creating a [LazyResetsCoroutineScope] instance with a pre-created [CoroutineScope].
 *
 * @param resetManager the [ResetManager][hermit.test.ResetManager]
 *   that manages the resetting of the CoroutineScope instance.
 * @param scope the pre-created [CoroutineScope] that is managed by the [LazyResetsCoroutineScope].
 * @param cleanUpTestCoroutines determines whether
 *   [cleanupTestCoroutines][TestCoroutineScope.cleanupTestCoroutines] should be
 *   invoked if the CoroutineScope is of type [TestCoroutineScope]. Defaults to true.
 * @return [LazyResetsCoroutineScope] instance that provides and resets the [CoroutineScope].
 */
@ExperimentalCoroutinesApi
public fun <T : CoroutineScope> lazyResetsCoroutineScope(
  resetManager: ResetManager,
  scope: T,
  cleanUpTestCoroutines: Boolean = true
): LazyResetsCoroutineScope<T> =
  LazyResetsCoroutineScopeImpl(resetManager, cleanUpTestCoroutines) { scope }

/**
 * Function for creating a [LazyResetsCoroutineScope] instance
 * with a factory method for creating the [CoroutineScope].
 *
 * @param resetManager the [ResetManager][hermit.test.ResetManager]
 *   that manages the resetting of the CoroutineScope instance.
 * @param cleanUpTestCoroutines determines whether
 *   [cleanupTestCoroutines][TestCoroutineScope.cleanupTestCoroutines] should be
 *   invoked if the CoroutineScope is of type [TestCoroutineScope]. Defaults to true.
 * @param scope the factory function responsible for creating the
 *   [CoroutineScope] that is managed by the [LazyResetsCoroutineScope].
 * @return [LazyResetsCoroutineScope] instance that provides and resets the [CoroutineScope].
 */
@ExperimentalCoroutinesApi
public fun <T : CoroutineScope> lazyResetsCoroutineScope(
  resetManager: ResetManager,
  cleanUpTestCoroutines: Boolean = true,
  scope: () -> T
): LazyResetsCoroutineScope<T> =
  LazyResetsCoroutineScopeImpl(resetManager, cleanUpTestCoroutines, scope)
