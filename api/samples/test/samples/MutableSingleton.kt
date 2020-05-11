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

package samples

import autoreset.api.*

object MutableSingleton : Resets {

  var sideEffect1: Int? = null
  var sideEffect2: Int? = null

  // assume this was added after the initial Resettable implementation
  var sideEffect3: Int? = null

  override fun reset() {
    sideEffect1 = null
    sideEffect2 = null

    // the updating author forgot to add sideEffect3 to the reset function!
  }
}
