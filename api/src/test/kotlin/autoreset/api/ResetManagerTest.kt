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

import io.kotest.core.spec.style.*
import io.kotest.matchers.*

internal class ResetManagerTest : FreeSpec(),
                                  FakeTest<ResetManager, ResetManager, TestResetManager> {

  override val subject1Factory: () -> ResetManager = { ResetManager() }

  override val subject2Factory: () -> TestResetManager = { TestResetManager() }

  init {

    "resetAll" - {

      "registered delegates should  be reset" - {
        runTest { manager ->

          val a = TestResets()
          val b = TestResets()

          manager.register(a)
          manager.register(b)

          a.numResets shouldBe 0
          b.numResets shouldBe 0

          manager.resetAll()

          a.numResets shouldBe 1
          b.numResets shouldBe 1
        }
      }

      "delegates should only be reset once unless re-registered" - {
        runTest { manager ->

          val a = TestResets()
          val b = TestResets()

          manager.register(a)
          manager.register(b)

          a.numResets shouldBe 0
          b.numResets shouldBe 0

          manager.resetAll()
          manager.resetAll()

          a.numResets shouldBe 1
          b.numResets shouldBe 1
        }
      }
    }
  }
}

internal class TestResets : Resets {

  var numResets = 0

  override fun reset() {
    synchronized(this) {
      numResets++
    }
  }
}
