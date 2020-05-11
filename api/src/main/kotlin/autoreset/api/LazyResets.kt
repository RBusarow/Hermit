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

package autoreset.api

import kotlin.reflect.*

/**
 * Lazy instance which can be reset.  After a reset, the next access will create a new instance.
 *
 * Each time a new instance is created, it is registered with the [ResetManager] and will be reset with the next [ResetManager.resetAll].
 *
 * @sample samples.LazyResetsSample.lazyResetClassSample
 */
public class LazyResets<out T : Any>(
  private val resetManager: ResetManager,
  private val valueFactory: () -> T
) : Lazy<T>, Resets {

  private var lazyHolder: Lazy<T> = createLazy()

  override val value: T
    get() = lazyHolder.value

  override fun isInitialized(): Boolean = lazyHolder.isInitialized()

  private fun createLazy() = lazy {
    resetManager.register(this)
    valueFactory()
  }

  override fun reset() {
    lazyHolder = createLazy()
  }
}

/**
 * Lazy instance which can be reset.  After a reset, the next access will create a new instance.
 *
 * Each time a new instance is created, it is registered with the [ResetManager] and will be reset with the next [ResetManager.resetAll].
 *
 * If the type being created can be initialized with a default constructor,
 * this function can be invoked without a [valueFactory] argument.
 *
 * If the type being created is an interface, abstract class, or does not have a default constructor,
 * then a factory must be specified.
 *
 * If the type being created is a Kotlin object, then it must implement the [Resets] interface
 *
 * @sample samples.LazyResetsSample.lazyResetClassSample
 * @sample samples.LazyResetsSample.lazyResetDelegateInImplementationSample
 */
public inline fun <reified T : Any> ResetManager.resets(
  noinline valueFactory: () -> T = {
    val clazz = T::class

    try {
      clazz.java.newInstance()
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
        clazz.isAbstract       -> throw LazyResetDelegateAbstractException(clazz)
        else                   -> throw LazyResetDelegateNonDefaultConstructorException(clazz)
      }
    }

  }
): LazyResets<T> = LazyResets(this, valueFactory)

/**
 * Classes without a default constructor cannot be used with a 'by resets' delegate.
 *
 * @see Class.newInstance
 */
public class LazyResetDelegateNonDefaultConstructorException(problemClass: KClass<*>) :
  LazyResetDelegateException(
    problemClass,
    "Classes without a default constructor cannot be used with a 'by resets' delegate."
  )

/**
 * Interfaces cannot be used with a 'by resetss' delegate since they cannot be instantiated.
 *
 * @see Class.newInstance
 */
public class LazyResetDelegateInterfaceException(problemClass: KClass<*>) :
  LazyResetDelegateException(
    problemClass,
    "Interfaces cannot be used with a 'by resets' delegate since they cannot be instantiated."
  )

/**
 * Abstract classes cannot be used with a 'by resets' delegate since they cannot be instantiated.
 *
 * @see Class.newInstance
 */
public class LazyResetDelegateAbstractException(problemClass: KClass<*>) :
  LazyResetDelegateException(
    problemClass,
    "Abstract classes cannot be used with a 'by resets' delegate since they cannot be instantiated."
  )

/**
 * Objects may not be used with a 'by resets' delegate unless they implement the LazyReset interface.
 *
 * @see KClass.objectInstance
 */
public class LazyResetDelegateObjectException(problemClass: KClass<*>) : LazyResetDelegateException(
  problemClass,
  "Objects may not be used with a 'by resets' delegate unless they implement the LazyReset interface."
)

public abstract class LazyResetDelegateException(
  problemClass: KClass<*>,
  message: String
) : Exception("\n\nerror initializing <${problemClass.qualifiedName}>. " + message)
