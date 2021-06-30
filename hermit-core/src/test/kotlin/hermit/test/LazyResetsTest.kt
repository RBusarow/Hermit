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

package hermit.test

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.checkAll
import kotlin.properties.Delegates

internal class LazyResetsTest : FreeSpec({

  var resetManager: TestResetManager by Delegates.notNull()

  beforeTest {
    resetManager = TestResetManager()
  }

  "types" - {

    "should be Lazy" - {

      val subject = LazyResets(resetManager) { 33 }

      subject.shouldBeInstanceOf<Lazy<Int>>()
    }

    "should be Resets" - {

      val subject = LazyResets(resetManager) { 33 }

      subject.shouldBeInstanceOf<Resets>()
    }
  }

  "isInitialized()" - {

    "should be false before `value` is called" - {

      val subject = LazyResets(resetManager) { 33 }

      subject.isInitialized() shouldBe false
    }

    "should be true after `value` is called" - {

      val subject = LazyResets(resetManager) { 33 }

      subject.value

      subject.isInitialized() shouldBe true
    }

    "should be false after reset" - {

      val subject = LazyResets(resetManager) { 33 }

      subject.value

      subject.isInitialized() shouldBe true

      subject.reset()

      subject.isInitialized() shouldBe false
    }
  }

  "value should be the value factory output" - {

    checkAll<String> { str ->

      val strLazy = LazyResets(resetManager) { str }

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

      val subject = LazyResets(resetManager) { 33 }

      subject.isInitialized() shouldBe false

      resetManager.delegates.shouldBeEmpty()
    }

    "should be registered after initialization" - {

      val subject = LazyResets(resetManager) { 33 }

      subject.value

      resetManager.delegates shouldBe listOf(subject)
    }
  }
})
