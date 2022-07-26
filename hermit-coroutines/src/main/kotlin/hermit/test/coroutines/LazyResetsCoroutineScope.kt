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
 * Binds a single instance of [CoroutineScope] to a [ResetManager].
 *
 * The same instance is re-used throughout the lifetime of the [LazyResetsCoroutineScope], since its
 * state is entirely reset in [reset][Resets.reset].
 *
 * ### reset behavior
 *
 * If the [scopeFactory] creates a [TestCoroutineScope] and [cleanUpTestCoroutines] is true,
 * [cleanupTestCoroutines][TestCoroutineScope.cleanupTestCoroutines] is called.
 *
 * If the [scopeFactory] does not create a [TestCoroutineScope] but its
 * [coroutineContext][kotlin.coroutines.CoroutineContext] contains a [Job], then [Job.cancel] is
 * called.
 *
 * If the [scopeFactory] does not create a [TestCoroutineScope] and does not have a [Job], then
 * [reset][Resets.reset] has no effect.
 *
 * @param cleanUpTestCoroutines if true and the dispatcher is a
 *   [TestCoroutineScope][kotlinx.coroutines.test.TestCoroutineScope], invoke
 *   [cleanupTestCoroutines][TestCoroutineScope.cleanupTestCoroutines]
 *   during reset. Has no effect for normal scopes.
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

public interface LazyResetsCoroutineScope<T : CoroutineScope> : LazyResets<T>

@ExperimentalCoroutinesApi
public fun <T : CoroutineScope> lazyResetsCoroutineScope(
  resetManager: ResetManager,
  scope: T,
  cleanUpTestCoroutines: Boolean = true
): LazyResetsCoroutineScope<T> =
  LazyResetsCoroutineScopeImpl(resetManager, cleanUpTestCoroutines) { scope }

@ExperimentalCoroutinesApi
public fun <T : CoroutineScope> lazyResetsCoroutineScope(
  resetManager: ResetManager,
  cleanUpTestCoroutines: Boolean = true,
  scope: () -> T
): LazyResetsCoroutineScope<T> =
  LazyResetsCoroutineScopeImpl(resetManager, cleanUpTestCoroutines, scope)
