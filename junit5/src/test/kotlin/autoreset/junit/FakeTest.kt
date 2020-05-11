/*
 * Copyright (C) 2020 Rick Busarow
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

package autoreset.junit

import io.kotest.assertions.*

interface FakeTest<T : Any, S1 : T, S2 : T> {

  val subject1Factory: () -> S1
  val subject2Factory: () -> S2

  fun afterSubject(subject: T) = Unit

}

inline fun <T : Any, S1 : T, S2 : T> FakeTest<T, S1, S2>.runTest(block: (T) -> Unit) {

  val subject1 = subject1Factory()

  "Error when testing against ${subject1::class.java.simpleName}".asClue {
    block(subject1)
  }

  afterSubject(subject1)

  val subject2 = subject2Factory()

  "Error when testing against ${subject2::class.java.simpleName}".asClue {
    block(subject2)
  }

  afterSubject(subject2)
}
