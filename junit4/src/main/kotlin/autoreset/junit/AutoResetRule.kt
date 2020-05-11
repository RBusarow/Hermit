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

import autoreset.api.*
import org.junit.rules.*
import org.junit.runner.*

/**
 * JUnit 4 [Rule][org.junit.Rule] which automatically resets the value
 * of a [LazyResets] instance after every test.
 *
 * Note that the default behavior for JUnit 4
 * is to create an entirely new test class instance for each test,
 * which means this would only be of value for resetting the state of singletons.
 *
 * @sample samples.AutoResetRuleSample
 */
class AutoResetRule(
  private val delegate: ResetManager = ResetManager()
) : TestWatcher(), ResetManager by delegate {

  override fun finished(description: Description?) {
    delegate.resetAll()
  }

}
