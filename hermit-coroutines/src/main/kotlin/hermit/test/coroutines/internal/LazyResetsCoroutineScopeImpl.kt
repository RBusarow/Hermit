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

package hermit.test.coroutines.internal

import hermit.test.ResetManager
import hermit.test.coroutines.LazyResetsCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.TestCoroutineScope

@ExperimentalCoroutinesApi
internal class LazyResetsCoroutineScopeImpl<T : CoroutineScope>(
  private val resetManager: ResetManager,
  private val cleanUpTestCoroutines: Boolean,
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
    @Suppress("DEPRECATION")
    if (scope is TestCoroutineScope && cleanUpTestCoroutines) {
      scope.cleanupTestCoroutines()
    } else {
      scope?.coroutineContext
        ?.get(Job)
        ?.cancelChildren()
    }
    lazyHolder = createLazy()
  }
}
