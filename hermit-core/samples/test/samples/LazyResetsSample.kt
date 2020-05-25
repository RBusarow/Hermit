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

package samples

import hermit.test.*

class LazyResetsSample : SampleTest() {

  @Sample
  fun lazyResetClassSample() {

    var instanceNumber = 0

    val resetManager = Hermit()

    val lazyInt = LazyResets(resetManager) {

      val newValue = ++instanceNumber

      println("initializing as $newValue")

      newValue
    }

    println("after declaration")

    // creates new instance, invoking the lambda
    println("access lazy --> ${lazyInt.value}")
    // instance already exists, so uses the same one and doesn't invoke lambda
    println("access lazy --> ${lazyInt.value}")

    println("resetting")
    resetManager.resetAll()

    // creates new instance, invoking the lambda again
    println("access lazy --> ${lazyInt.value}")

    output shouldPrint """after declaration
      |initializing as 1
      |access lazy --> 1
      |access lazy --> 1
      |resetting
      |initializing as 2
      |access lazy --> 2
    """.trimMargin()
  }

  @Sample
  fun lazyResetDelegateSample() {

    var instanceNumber = 0

    val resetManager = Hermit()

    val lazyInt by resetManager.resets {

      val newValue = ++instanceNumber

      println("initializing as $newValue")

      newValue
    }

    println("after declaration")

    // creates new instance, invoking the lambda
    println("access lazy --> $lazyInt")
    // instance already exists, so uses the same one and doesn't invoke lambda
    println("access lazy --> $lazyInt")

    println("resetting")
    resetManager.resetAll()

    // creates new instance, invoking the lambda again
    println("access lazy --> $lazyInt")

    output shouldPrint """after declaration
      |initializing as 1
      |access lazy --> 1
      |access lazy --> 1
      |resetting
      |initializing as 2
      |access lazy --> 2
    """.trimMargin()
  }

  @Sample
  fun lazyResetDelegateInImplementationSample() {

    class SomeImplementation : Hermit() {

      var instanceNumber = 0

      val lazyInt by resets {

        val newValue = ++instanceNumber

        println("initializing as $newValue")

        newValue
      }

    }

    val impl = SomeImplementation()

    println("after declaration")

    // creates new instance, invoking the lambda
    println("access lazy --> ${impl.lazyInt}")
    // instance already exists, so uses the same one and doesn't invoke lambda
    println("access lazy --> ${impl.lazyInt}")

    println("resetting")
    impl.resetAll()

    // creates new instance, invoking the lambda again
    println("access lazy --> ${impl.lazyInt}")

    output shouldPrint """after declaration
      |initializing as 1
      |access lazy --> 1
      |access lazy --> 1
      |resetting
      |initializing as 2
      |access lazy --> 2
    """.trimMargin()
  }

}

