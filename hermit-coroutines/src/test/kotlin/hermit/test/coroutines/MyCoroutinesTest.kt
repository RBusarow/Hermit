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

package hermit.test.coroutines

import hermit.test.coroutines.MyCoroutinesTest.MyEnvironment
import io.kotest.matchers.result.shouldBeSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class MyCoroutinesTest : HermitCoroutines<MyEnvironment>(::MyEnvironment) {

  class MyEnvironment(
    override val testScope: TestScope
  ) : TestEnvironmentScope() {

    val someRepository = SomeRepository(testScope)
    val myWorker = MyWorker(backgroundScope)
  }

  @Test
  fun `some test`() = test {

    myWorker.doWork()

    advanceTimeBy(500)

    someRepository.getStuff() shouldBeSuccess SomeData()
  }

  @Test
  fun `some other test`() = test {

    println("my unique repository instance -- $someRepository")
  }
}
