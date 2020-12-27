package samples

import dispatch.test.*
import hermit.test.coroutines.*
import hermit.test.junit.*
import io.kotest.assertions.throwables.*
import io.kotest.matchers.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.*

@ExperimentalCoroutinesApi
class ResetsScopeSample : HermitJUnit5() {

  val testScope by resetsScope(TestCoroutineScope())
  val providedScope by resetsScope<TestProvidedCoroutineScope>()
  val normalScope: CoroutineScope by resetsScope()

  @Test
  fun `resetAll should throw an exception if a TestCoroutineScope is leaking`() =
    runBlocking<Unit> {
      testScope.leak()

      shouldThrow<UncompletedCoroutinesError> {
        resetAll()
      }

      providedScope.leak()

      shouldThrow<UncompletedCoroutinesError> {
        resetAll()
      }
    }

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
