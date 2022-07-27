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
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.freeSpec
import io.kotest.matchers.shouldBe

internal class AutoResetFactoryTest : FreeSpec({

  include(managerTests(Hermit()))
  include(managerTests(AutoReset()))
  include(managerTests(object : HermitJUnit5() {}))
}) {
  override fun isolationMode(): IsolationMode? = IsolationMode.InstancePerLeaf
}

private inline fun <reified T : ResetManager> managerTests(manager: T) = freeSpec {
  manager::class.toString() - {
    "resetAll" - {
      "registered delegates should  be reset" - {
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

internal class TestResets : Resets {

  var numResets = 0

  override fun reset() {
    synchronized(this) {
      numResets++
    }
  }
}
