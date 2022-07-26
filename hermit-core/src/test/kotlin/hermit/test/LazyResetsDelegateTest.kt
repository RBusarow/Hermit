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

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.checkAll
import kotlin.properties.Delegates

internal class LazyResetsDelegateTest : FreeSpec({

  var resetManager: TestResetManager by Delegates.notNull()

  beforeTest {
    resetManager = TestResetManager()
  }

  "types" - {

    "should be LazyResets" - {

      val subject = resetManager.resets { 33 }

      subject.shouldBeInstanceOf<LazyResets<Int>>()
    }

    "should be Lazy" - {

      val subject = resetManager.resets { 33 }

      subject.shouldBeInstanceOf<Lazy<Int>>()
    }

    "should be Resets" - {

      val subject = resetManager.resets { 33 }

      subject.shouldBeInstanceOf<Resets>()
    }
  }

  "isInitialized()" - {

    "should be false before `value` is called" - {

      val subject = resetManager.resets { 33 }

      subject.isInitialized() shouldBe false
    }

    "should be true after `value` is called" - {

      val subject = resetManager.resets { 33 }

      subject.value

      subject.isInitialized() shouldBe true
    }

    "should be false after reset" - {

      val subject = resetManager.resets { 33 }

      subject.value

      subject.isInitialized() shouldBe true

      subject.reset()

      subject.isInitialized() shouldBe false
    }
  }

  "value should be the value factory output" - {

    checkAll<String> { str ->

      val strLazy = resetManager.resets { str }

      strLazy.value shouldBe str
    }
  }

  "value factory should be re-invoked for each reset" - {

    checkAll<String, String> { a, b ->

      var hasReset = false

      val initBlock = suspend { if (!hasReset) a else b }

      val subject = LazyResets(resetManager, initBlock)

      subject.value shouldBe a

      hasReset = true
      resetManager.resetAll()

      subject.value shouldBe b
    }
  }

  "registration with resetManager" - {

    "should not be registered if not initialized" - {

      val subject = resetManager.resets { 33 }

      subject.isInitialized() shouldBe false

      resetManager.delegates.shouldBeEmpty()
    }

    "should be registered after initialization" - {

      val subject = resetManager.resets { 33 }

      subject.value

      resetManager.delegates shouldBe listOf(subject)
    }
  }

  "value factory behavior" - {

    "explicit factory declaration should execute normally" - {

      checkAll<Int> { i ->
        val subject = resetManager.resets { i }

        subject.value shouldBe i
      }
    }

    "no-factory initialization" - {

      "happy path" - {

        "classes with default constructors should work" - {

          val subject = resetManager.resets<DefaultConstructor>()

          subject.value
        }

        "classes with default parameters for non-default constructors should work" - {

          val subject = resetManager.resets<ConstructorWithDefaultValues>()

          subject.value
        }

        "open classes with default constructors should work" - {

          val subject = resetManager.resets<OpenClass>()

          subject.value
        }

        "objects which implement Resets should work" - {

          val subject = resetManager.resets<ObjectWithResets>()

          subject.value
        }
      }

      "sad path" - {

        "normal objects should throw LazyResetDelegateObjectException" - {

          val subject = resetManager.resets<ObjectWithoutResets>()

          shouldThrow<LazyResetDelegateObjectException> {
            subject.value
          }
        }

        "abstract classes should throw LazyResetDelegateObjectException" - {

          val subject = resetManager.resets<AbstractClass>()

          shouldThrow<LazyResetDelegateAbstractException> {
            subject.value
          }
        }

        "interfaces should throw LazyResetDelegateInterfaceException" - {

          val subject = resetManager.resets<Interface>()

          shouldThrow<LazyResetDelegateInterfaceException> {
            subject.value
          }
        }

        "classes without default constructor should throw LazyResetDelegateNonDefaultConstructorException" - {

          val subject = resetManager.resets<NoDefaultConstructor>()

          shouldThrow<LazyResetDelegateNonDefaultConstructorException> {
            subject.value
          }
        }
      }
    }
  }
})

/*
Should all work without factories
 */
internal class DefaultConstructor
internal class ConstructorWithDefaultValues(val name: String = "default")
internal open class OpenClass
internal object ObjectWithResets : Resets {
  override fun reset() = Unit
}

/*
Should all require factories
 */
internal class NoDefaultConstructor(val name: String)
internal interface Interface
internal abstract class AbstractClass
internal object ObjectWithoutResets
