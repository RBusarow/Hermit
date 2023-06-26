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

package hermit.test.junit

import hermit.test.Hermit
import hermit.test.ResetManager
import hermit.test.Resets
import kotlinx.atomicfu.atomic
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestInstancePostProcessor

public class HermitExtension(
  resetManager: ResetManager = Hermit()
) : TestInstancePostProcessor,
  AfterEachCallback,
  ResetManager {

  private val tempDelegates: MutableList<Resets> = mutableListOf()

  private val topInstanceProcessed = atomic(false)

  private var delegate: ResetManager = resetManager
    set(new) {
      field = new
      tempDelegates.clear()
    }

  override fun register(delegate: Resets) {
    if (topInstanceProcessed.value) {
      this.delegate.register(delegate)
    } else {
      synchronized(this) {
        tempDelegates.add(delegate)
      }
    }
  }

  override fun resetAll() {
    delegate.resetAll()
    synchronized(this) {
      tempDelegates.forEach { it.reset() }
      tempDelegates.clear()
    }
  }

  override fun postProcessTestInstance(testInstance: Any?, context: ExtensionContext?) {
    /*
    Nested classes also trigger `postProcessTestInstance`,
    so add a check to only set the instance for the first (top-most) instance.
     */
    if (!topInstanceProcessed.value) {
      (testInstance as? ResetManager)?.let {
        delegate = it
      }
    }

    topInstanceProcessed.compareAndSet(expect = false, update = true)
  }

  override fun afterEach(context: ExtensionContext?) {
    resetAll()
  }
}
