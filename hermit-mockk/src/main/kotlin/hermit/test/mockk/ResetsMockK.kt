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

package hermit.test.mockk

import hermit.test.ResetManager
import hermit.test.Resets
import io.mockk.MockKException
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import kotlin.reflect.KClass

/**
 * Lazy MockK object for which [reset] will call [clearMocks].
 *
 * # Lifecycle
 *
 * ## On initialization
 * - creates a MockK instance
 * - *if `block` was specified* sets all answers from the `every { ... }` `block` argument
 *
 * ## On first access after reset
 * - registers the [mock] with the [resetManager]
 *
 * ## On repeated access
 * - returns the same [mock] instance and does nothing with `block`
 *
 * ## On reset
 * - calls [clearMocks(mock)][clearMocks] and eagerly invokes
 *   `block` again to reset the state to the initial one
 *
 * **Implementation Note**
 *
 * This behavior of eagerly invoking the answers `block` is atypical for this
 * library. It is necessary because otherwise, it is likely that the first
 * access of the [mock] instance would be inside an [every] block. Attempting
 * to invoke [every] while already inside [every] causes a [MockKException].
 *
 * @property resetManager The [ResetManager] which is shared
 *   by all mocks and other resettable fields in the test.
 * @property mock The mock instance exposed by the Lazy delegate.
 * @property clearPolicy Describes the parameters passed via [clearMocks] when invoking [reset]
 * @property block The answers ( `every { ... } returns ...` ) applied after a reset.
 * @sample samples.SimpleTest
 * @sample samples.ComplexTest
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

  public class ClearPolicy(
    public val answers: Boolean = true,
    public val recordedCalls: Boolean = true,
    public val childMocks: Boolean = true,
    public val verificationMarks: Boolean = true,
    public val exclusionRules: Boolean = true
  ) {
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is ClearPolicy) return false

      if (answers != other.answers) return false
      if (recordedCalls != other.recordedCalls) return false
      if (childMocks != other.childMocks) return false
      if (verificationMarks != other.verificationMarks) return false
      return exclusionRules == other.exclusionRules
    }

    override fun hashCode(): Int {
      var result = answers.hashCode()
      result = 31 * result + recordedCalls.hashCode()
      result = 31 * result + childMocks.hashCode()
      result = 31 * result + verificationMarks.hashCode()
      result = 31 * result + exclusionRules.hashCode()
      return result
    }

    override fun toString(): String {
      return buildString {
        append("ClearPolicy(answers=")
        append(answers)
        append(", recordedCalls=")
        append(recordedCalls)
        append(", childMocks=")
        append(childMocks)
        append(", verificationMarks=")
        append(verificationMarks)
        append(", exclusionRules=")
        append(exclusionRules)
        append(")")
      }
    }
  }
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
 * * calls [clearMocks(mock)][clearMocks] and eagerly invokes
 *   `block` again to reset the state to the initial one
 *
 * **Implementation Note**
 *
 * This behavior of eagerly invoking the answers `block` is atypical for this
 * library. It is necessary because otherwise, it is likely that the first
 * access of the mock instance would be inside an [every] block. Attempting
 * to invoke [every] while already inside [every] causes a [MockKException].
 *
 * @sample samples.SimpleTest
 * @sample samples.ComplexTest
 * @see mockk
 * @see ResetsMockK
 */
@Suppress(
  "SpreadOperator",
  "LongParameterList"
)
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
    relaxUnitFun = relaxUnitFun,
    moreInterfaces = moreInterfaces
  ),
  clearPolicy = clearPolicy,
  block = block
)
