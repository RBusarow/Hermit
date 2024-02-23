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

package hermit.test.internal

import hermit.test.LazyResets
import hermit.test.ResetManager
import kotlinx.coroutines.runBlocking

/**
 * Lazy instance which can be reset. After a reset, the next access will create a new instance.
 *
 * Each time a new instance is created, it is registered with the
 * [ResetManager] and will be reset with the next [ResetManager.resetAll].
 *
 * @sample samples.LazyResetsSample.lazyResetClassSample
 */
internal class LazyResetsImpl<out T : Any>(
  private val resetManager: ResetManager,
  private val valueFactory: suspend () -> T
) : LazyResets<T> {
  private var lazyHolder: Lazy<T> = createLazy()

  override val value: T
    get() = lazyHolder.value

  override fun isInitialized(): Boolean = lazyHolder.isInitialized()

  private fun createLazy() =
    lazy {
      resetManager.register(this)
      runBlocking { valueFactory() }
    }

  override fun reset() {
    lazyHolder = createLazy()
  }
}
