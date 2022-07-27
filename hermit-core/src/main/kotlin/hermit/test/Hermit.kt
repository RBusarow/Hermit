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

package hermit.test

/**
 * Factory for creating a default [ResetManager] instance.
 *
 * All calls to [register][ResetManager.register] and [resetAll][ResetManager.resetAll] are thread-safe.
 *
 * @sample samples.DelegatingResetManagerImpl
 */
public open class Hermit(
  private val delegates: MutableCollection<Resets> = mutableListOf()
) : ResetManager {

  override fun register(delegate: Resets) {
    synchronized(delegates) {
      delegates.add(delegate)
    }
  }

  override fun resetAll() {
    synchronized(delegates) {
      delegates.forEach { it.reset() }
      delegates.clear()
    }
  }
}
