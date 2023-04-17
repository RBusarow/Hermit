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

import hermit.test.Hermit2Test.MyTestEnvironment
import org.junit.jupiter.api.Test
import java.util.UUID

internal class Hermit2Test : Hermit2<MyTestEnvironment>(
  MyTestEnvironment::class
) {

  public class MyTestEnvironment {
    public val someId: String = UUID.randomUUID().toString()
  }

  @Test
  fun `test 1`() = test {
    println("test 2 environment id is -- $someId")
  }

  @Test
  fun `test 2`() = test {
    println("test 2 environment id is -- $someId")
  }
}