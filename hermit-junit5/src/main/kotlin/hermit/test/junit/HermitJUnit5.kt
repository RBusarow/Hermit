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
import hermit.test.LazyResetDelegateAbstractException
import hermit.test.LazyResetDelegateInterfaceException
import hermit.test.LazyResetDelegateNonDefaultConstructorException
import hermit.test.LazyResetDelegateObjectException
import hermit.test.LazyResets
import hermit.test.Resets
import org.junit.jupiter.api.AfterEach
import hermit.test.resets as resetsExtension

@Suppress("UnnecessaryAbstractClass")
public abstract class HermitJUnit5(
  delegates: MutableCollection<Resets> = mutableListOf()
) : Hermit(delegates),
  AutoReset {

  @Suppress("FunctionName")
  @AfterEach
  internal fun _afterEach() {
    resetAll()
  }

  /**
   * Convenience function which delegates to
   * [ResetManager.resets][resetsExtension] without needing the import.
   *
   * @see resetsExtension
   */
  public inline fun <reified T : Any> resets(
    noinline valueFactory: suspend () -> T = {
      val clazz = T::class

      @Suppress("SwallowedException")
      try {
        clazz.java.getDeclaredConstructor().newInstance()
      } catch (illegal: IllegalAccessException) {
        val obj = clazz.objectInstance

        if (obj != null && obj is Resets) {
          obj
        } else {
          throw LazyResetDelegateObjectException(clazz)
        }
      } catch (abstract: InstantiationException) {
        when {
          clazz.java.isInterface -> throw LazyResetDelegateInterfaceException(clazz)
          clazz.isAbstract -> throw LazyResetDelegateAbstractException(clazz)
          else -> throw LazyResetDelegateNonDefaultConstructorException(clazz)
        }
      }
    }
  ): LazyResets<T> = resetsExtension(valueFactory)
}
