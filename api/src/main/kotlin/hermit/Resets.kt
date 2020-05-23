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

package hermit

/**
 * Marks any type which may be reset.
 *
 * Note that some implementations of `Resettable` are lower maintenance (and therefore safer) than others.
 *
 * It's reasonably safe to implement `Resettable` when all state
 * can be reset in a single "nuclear" option,
 * since future updates would automatically also be captured.
 *
 * @sample samples.ResettableMap
 *
 * On the other hand, if each piece of state needs to be handled explicitly,
 * then it is far more likely that future additions state may forget to update the `reset` function.
 *
 * @sample samples.MutableSingleton
 */
interface Resets {
  fun reset()
}

