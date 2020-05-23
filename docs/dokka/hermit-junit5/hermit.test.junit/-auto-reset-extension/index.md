[hermit-junit5](../../index.md) / [hermit.test.junit](../index.md) / [AutoResetExtension](./index.md)

# AutoResetExtension

`class AutoResetExtension : TestInstancePostProcessor, AfterEachCallback, `[`AutoReset`](../-auto-reset.md) [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-junit5/src/main/kotlin/hermit/test/junit/AutoReset.kt#L37)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `AutoResetExtension(autoReset: `[`AutoReset`](../-auto-reset.md)` = AutoReset())` |

### Functions

| Name | Summary |
|---|---|
| [afterEach](after-each.md) | `fun afterEach(context: ExtensionContext?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [postProcessTestInstance](post-process-test-instance.md) | `fun postProcessTestInstance(testInstance: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, context: ExtensionContext?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [register](register.md) | `fun register(delegate: `[`Resets`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-resets/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [resetAll](reset-all.md) | `fun resetAll(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
