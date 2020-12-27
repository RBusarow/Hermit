package hermit.test.junit

import hermit.test.*
import io.kotest.matchers.*
import org.junit.*
import org.junit.runners.*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HermitJUnit4Test : HermitJUnit4(HermitJUnit4Test) {

  companion object : Hermit() {
    var resetCount = 0
    val subject by resets { TestSubject() }
  }

  @After
  fun after() {
    resetCount++
  }

  @Test
  fun `test 1`() {
    resetCount shouldBe 0
    ++subject.count shouldBe 1
  }

  @Test
  fun `test 2 -- executes second but should have a fresh state`() {
    resetCount shouldBe 1
    subject.count shouldBe 0
  }

  class TestSubject {
    var count = 0
  }
}
