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

package samples

import hermit.test.Hermit
import hermit.test.ResetManager
import hermit.test.resets

class DelegatingResetManagerImpl : ResetManager by Hermit() {

  // auto-registered with this ResetManager
  val someResettable by resets { SomeClass() }

  init {

    // initializes the someResettable instance
    someResettable

    // resets someResettable
    resetAll()
  }
}

class SomeClass
