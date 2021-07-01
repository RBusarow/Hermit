package hermit.test.coroutines

import dispatch.test.TestProvidedCoroutineScope
import hermit.test.junit.HermitJUnit5
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.UncompletedCoroutinesError
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExperimentalCoroutinesApi
internal class LazyResetsCoroutineScopeTest : HermitJUnit5() {

  val testScope by resetsScope { TestCoroutineScope() }
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
