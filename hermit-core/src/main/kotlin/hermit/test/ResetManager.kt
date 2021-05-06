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

package hermit.test

/**
 * Marks a class which is capable of tracking and resetting multiple [Resets] instances.
 *
 * @sample samples.UnsafeResetManager
 * @see [ResetManager]
 */
public interface ResetManager {

  public fun register(delegate: Resets)
  public fun resetAll()
}
