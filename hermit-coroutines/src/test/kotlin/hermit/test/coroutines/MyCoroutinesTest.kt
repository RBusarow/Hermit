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

import hermit.test.coroutines.HermitCoroutines.TestEnvironmentScope
import hermit.test.coroutines.MyCoroutinesTest.MyEnvironment
import io.kotest.matchers.result.shouldBeSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.coroutines.CoroutineContext
import kotlin.time.TimeSource

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

/**
 * The base class for test classes.
 *
 * This could definitely be broken down into an interface with a concrete delegate, to work with
 * classes which already have a base class.
 */
@ExperimentalCoroutinesApi
abstract class HermitCoroutines<E : TestEnvironmentScope>(
  private val factory: (TestScope) -> E
) {

  /**
   * the base class for the environment... It can't implement `TestScope`, so instead it just
   * decorates. If you need an actual `TestScope`-typed instance to pass into something, it's still
   * available.
   */
  abstract class TestEnvironmentScope : CoroutineScope {

    abstract val testScope: TestScope

    val testScheduler: TestCoroutineScheduler
      get() = testScope.testScheduler

    val backgroundScope: CoroutineScope
      get() = testScope.backgroundScope

    final override val coroutineContext: CoroutineContext
      get() = testScope.coroutineContext
  }

  /**
   * Uses `runTest { ... }` to create and manage a TestScope, creating a new instance of the
   * environment for each test.
   */
  fun test(
    action: suspend E.() -> Unit
  ) {

    runTest {

      val instance = factory(this@runTest)

      instance.action()
    }
  }

  /*
  mirrors of the TestScope extension functions from kotlinx-coroutines `TestScope.kt`
   */

  val TestEnvironmentScope.currentTime: Long
    get() = testScheduler.currentTime

  fun TestEnvironmentScope.advanceUntilIdle(): Unit = testScheduler.advanceUntilIdle()
  fun TestEnvironmentScope.runCurrent(): Unit = testScheduler.runCurrent()
  fun TestEnvironmentScope.advanceTimeBy(delayTimeMillis: Long): Unit =
    testScheduler.advanceTimeBy(delayTimeMillis)

  val TestEnvironmentScope.testTimeSource: TimeSource get() = testScheduler.timeSource
}
