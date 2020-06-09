[hermit-junit5](../../index.md) / [hermit.test.junit](../index.md) / [HermitExtension](./index.md)

# HermitExtension

`class HermitExtension : TestInstancePostProcessor, AfterEachCallback, `[`ResetManager`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md) [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-junit5/src/main/kotlin/hermit/test/junit/HermitExtension.kt#L7)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `HermitExtension(resetManager: `[`ResetManager`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md)` = Hermit())` |

### Functions

| Name | Summary |
|---|---|
| [afterEach](after-each.md) | `fun afterEach(context: ExtensionContext?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [postProcessTestInstance](post-process-test-instance.md) | `fun postProcessTestInstance(testInstance: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, context: ExtensionContext?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [register](register.md) | `fun register(delegate: `[`Resets`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-resets/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [resetAll](reset-all.md) | `fun resetAll(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
