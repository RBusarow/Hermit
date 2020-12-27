package hermit.test.coroutines

import dispatch.test.*
import hermit.test.junit.*
import io.kotest.assertions.throwables.*
import io.kotest.matchers.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExperimentalCoroutinesApi
internal class LazyResetsCoroutineScopeTest : HermitJUnit5() {

  val testScope by resetsScope(TestCoroutineScope())
  val providedScope by resetsScope<TestProvidedCoroutineScope>()
  val normalScope: CoroutineScope by resetsScope()

  val scopeInstances = mutableSetOf<CoroutineScope>()

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

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
  inner class `instance retention` {

    @Order(1)
    @Test
    fun `adding scopes to set`() {
      scopeInstances.add(testScope)
      scopeInstances.add(providedScope)
      scopeInstances.add(normalScope)

      scopeInstances.size shouldBe 3
    }

    @Order(2)
    @Test
    fun `adding current scopes to set should still only have 3`() {
      scopeInstances.size shouldBe 3

      scopeInstances.add(testScope)
      scopeInstances.add(providedScope)
      scopeInstances.add(normalScope)

      scopeInstances.size shouldBe 3
    }
  }

  fun CoroutineScope.leak() = launch {
    while (true) {
      delay(10)
    }
  }
}
