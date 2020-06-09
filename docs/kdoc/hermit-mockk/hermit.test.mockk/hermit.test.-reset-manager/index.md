[hermit-mockk](../../index.md) / [hermit.test.mockk](../index.md) / [hermit.test.ResetManager](./index.md)

### Extensions for hermit.test.ResetManager

| Name | Summary |
|---|---|
| [resetsMockk](resets-mockk.md) | Creates a [ResetsMockK](../-resets-mock-k/index.md) object for which [reset](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-resets/reset.md) will call [clearMocks](#).`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`ResetManager`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md)`.resetsMockk(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, relaxed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, vararg moreInterfaces: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>, relaxUnitFun: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, clearPolicy: ClearPolicy = ResetsMockK.ClearPolicy(), block: T.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = {}): `[`ResetsMockK`](../-resets-mock-k/index.md)`<T>` |
