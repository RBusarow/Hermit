package hermit.test.mockk

import hermit.test.*
import io.mockk.*
import kotlin.reflect.*

/**
 * Lazy MockK object for which [reset] will call [clearMocks].
 *
 * # Lifecycle
 *
 * ## On initialization
 * * creates a MockK instance
 * * *if `block` was specified* sets all answers from the `every { ... }` `block` argument
 *
 * ## On first access after reset
 * * registers the [mock] with the [resetManager]
 *
 * ## On repeated access
 * * returns the same [mock] instance and does nothing with `block`
 *
 * ## On reset
 * * calls [clearMocks(mock)][clearMocks] and eagerly invokes `block` again to reset the state to the initial one
 *
 * **Implementation Note**
 *
 * This behavior of eagerly invoking the answers `block` is atypical for this library.  It is necessary
 * because otherwise, it is likely that the first access of the [mock] instance would be inside an [every] block.
 * Attempting to invoke [every] while already inside [every] causes a [MockKException].
 *
 * @sample samples.SimpleTest
 * @sample samples.ComplexTest
 * @param resetManager The [ResetManager] which is shared by all mocks and other resettable fields in the test.
 * @param mock The mock instance exposed by the Lazy delegate.
 * @param clearPolicy Describes the parameters passed via [clearMocks] when invoking [reset]
 * @param block The answers ( `every { ... } returns ...` ) applied after a reset.
 */
public class ResetsMockK<out T : Any>(
  private val resetManager: ResetManager,
  private val mock: T,
  private val clearPolicy: ClearPolicy = ClearPolicy(),
  private val block: T.() -> Unit = {}
) : Lazy<T>,
    Resets {

  init {
    mock.block()
  }

  private var lazyHolder: Lazy<T> = createLazy()

  override val value: T
    get() = lazyHolder.value

  override fun isInitialized(): Boolean = lazyHolder.isInitialized()

  private fun createLazy() = lazy {
    resetManager.register(this)
    mock
  }

  override fun reset() {
    lazyHolder = createLazy()
    clearMocks(
      firstMock = mock,
      answers = clearPolicy.answers,
      recordedCalls = clearPolicy.recordedCalls,
      childMocks = clearPolicy.childMocks,
      verificationMarks = clearPolicy.verificationMarks,
      exclusionRules = clearPolicy.exclusionRules
    )
    mock.block()
  }

  data class ClearPolicy(
    val answers: Boolean = true,
    val recordedCalls: Boolean = true,
    val childMocks: Boolean = true,
    val verificationMarks: Boolean = true,
    val exclusionRules: Boolean = true
  )
}

/**
 * Creates a [ResetsMockK] object for which [reset][Resets.reset] will call [clearMocks].
 *
 * # Lifecycle
 *
 * ## On initialization
 * * creates a MockK instance
 * * *if `block` was specified* sets all answers from the `every { ... }` `block` argument
 *
 * ## On first access after reset
 * * registers the mock with the [ResetManager] receiver
 *
 * ## On repeated access
 * * returns the same mock instance and does nothing with `block`
 *
 * ## On reset
 * * calls [clearMocks(mock)][clearMocks] and eagerly invokes `block` again to reset the state to the initial one
 *
 * **Implementation Note**
 *
 * This behavior of eagerly invoking the answers `block` is atypical for this library.  It is necessary
 * because otherwise, it is likely that the first access of the mock instance would be inside an [every] block.
 * Attempting to invoke [every] while already inside [every] causes a [MockKException].
 *
 * @sample samples.SimpleTest
 * @sample samples.ComplexTest
 * @see mockk
 * @see ResetsMockK
 */
public inline fun <reified T : Any> ResetManager.resetsMockk(
  name: String? = null,
  relaxed: Boolean = false,
  vararg moreInterfaces: KClass<*>,
  relaxUnitFun: Boolean = false,
  clearPolicy: ResetsMockK.ClearPolicy = ResetsMockK.ClearPolicy(),
  noinline block: T.() -> Unit = {}
): ResetsMockK<T> = ResetsMockK(
  resetManager = this,
  mock = mockk(
    name = name,
    relaxed = relaxed,
    moreInterfaces = *moreInterfaces,
    relaxUnitFun = relaxUnitFun
  ),
  clearPolicy = clearPolicy,
  block = block
)
