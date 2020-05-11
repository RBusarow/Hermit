/*
 * Copyright (C) 2020 Rick Busarow
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

package autoreset.junit

import autoreset.api.*
import kotlinx.atomicfu.*
import org.junit.jupiter.api.extension.*

/**
 * @sample samples.AutoResetClassDelegationSample
 */
@ExtendWith(AutoResetExtension::class)
interface AutoReset : ResetManager

fun AutoReset(): AutoReset = object : AutoReset {

  private val resetManager = ResetManager()

  override fun register(delegate: Resets) = resetManager.register(delegate)

  override fun resetAll() = resetManager.resetAll()
}

class AutoResetExtension(autoReset: AutoReset = AutoReset()) : TestInstancePostProcessor,
                                                               AfterEachCallback,
                                                               AutoReset {

  private var tempDelegates: MutableList<Resets>? = mutableListOf()

  private val topInstanceProcessed = atomic(false)

  private var delegate: AutoReset = autoReset
    set(new) {
      field = new
      tempDelegates = null
    }

  override fun register(delegate: Resets) {
    if (topInstanceProcessed.value) {
      this.delegate.register(delegate)
    } else {
      synchronized(this) {
        tempDelegates?.add(delegate)
      }
    }
  }

  override fun resetAll() {
    delegate.resetAll()
    synchronized(this) {
      tempDelegates?.forEach { it.reset() }
      tempDelegates?.clear()
    }
  }

  override fun postProcessTestInstance(testInstance: Any?, context: ExtensionContext?) {

    /*
    Nested classes also trigger `postProcessTestInstance`,
    so add a check to only set the instance for the first (top-most) instance.
     */
    if (!topInstanceProcessed.value) {

      (testInstance as? AutoReset)?.let {
        delegate = it
      }
    }

    topInstanceProcessed.compareAndSet(expect = false, update = true)
  }

  override fun afterEach(context: ExtensionContext?) {
    resetAll()
  }
}
