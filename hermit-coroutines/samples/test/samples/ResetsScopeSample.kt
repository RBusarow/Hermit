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

package samples

import dispatch.test.TestProvidedCoroutineScope
import hermit.test.coroutines.resetsScope
import hermit.test.junit.HermitJUnit5
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class ResetsScopeSample : HermitJUnit5() {

  val testScope by resetsScope { TestScope() }
  val providedScope by resetsScope<TestProvidedCoroutineScope>()
  val normalScope: CoroutineScope by resetsScope()

  @Test
  fun `resetAll should cancel all child coroutines if not a test scope`() = runBlocking<Unit> {
    val leakyJob = normalScope.leak()

    resetAll()

    leakyJob.isCancelled shouldBe true
  }

  fun CoroutineScope.leak() = launch {
    while (true) {
      delay(10)
    }
  }
}
