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

package hermit.test

import io.kotest.core.spec.style.FreeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

@Suppress("MaxLineLength", "Unused")
class ExceptionsTest :
  FreeSpec({

    "types" - {

      "should all be Exception" - {

        forAll(
          row(object : LazyResetDelegateException(String::class, "") {}),
          row(LazyResetDelegateNonDefaultConstructorException(String::class)),
          row(LazyResetDelegateInterfaceException(String::class)),
          row(LazyResetDelegateAbstractException(String::class)),
          row(LazyResetDelegateObjectException(String::class))
        ) { exception ->

          exception.shouldBeInstanceOf<Exception>()
        }
      }

      "should all be LazyResetDelegateException" - {

        forAll(
          row(LazyResetDelegateNonDefaultConstructorException(String::class)),
          row(LazyResetDelegateInterfaceException(String::class)),
          row(LazyResetDelegateAbstractException(String::class)),
          row(LazyResetDelegateObjectException(String::class))
        ) { exception ->

          exception.shouldBeInstanceOf<LazyResetDelegateException>()
        }
      }
    }

    "non default constructor exception message" - {

      LazyResetDelegateNonDefaultConstructorException(String::class).message shouldBe
        """


        error initializing <kotlin.String>. Classes without a default constructor cannot be used with a 'by resets' delegate.
        """.trimIndent()
    }

    "interface exception message" - {

      LazyResetDelegateInterfaceException(String::class).message shouldBe
        """


        error initializing <kotlin.String>. Interfaces cannot be used with a 'by resets' delegate since they cannot be instantiated.
        """.trimIndent()
    }

    "abstract class exception message" - {

      LazyResetDelegateAbstractException(String::class).message shouldBe
        """


        error initializing <kotlin.String>. Abstract classes cannot be used with a 'by resets' delegate since they cannot be instantiated.
        """.trimIndent()
    }

    "object without Resets implementation exception message" - {

      LazyResetDelegateObjectException(String::class).message shouldBe
        """


        error initializing <kotlin.String>. Objects may not be used with a 'by resets' delegate unless they implement the LazyReset interface.
        """.trimIndent()
    }
  })
