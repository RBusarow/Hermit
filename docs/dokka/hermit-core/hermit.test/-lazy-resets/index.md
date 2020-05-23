[hermit-core](../../index.md) / [hermit.test](../index.md) / [LazyResets](./index.md)

# LazyResets

`class LazyResets<out T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Lazy`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-lazy/index.html)`<T>, `[`Resets`](../-resets/index.md) [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-core/src/main/kotlin/hermit/test/LazyResets.kt#L27)

Lazy instance which can be reset.  After a reset, the next access will create a new instance.

Each time a new instance is created, it is registered with the [ResetManager](../-reset-manager/index.md) and will be reset with the next [ResetManager.resetAll](../-reset-manager/reset-all.md).

``` kotlin
var instanceNumber = 0

val resetManager = ResetManager()

val lazyInt = LazyResets(resetManager) {

  val newValue = ++instanceNumber

  println("initializing as $newValue")

  newValue
}

println("after declaration")

// creates new instance, invoking the lambda
println("access lazy --> ${lazyInt.value}")
// instance already exists, so uses the same one and doesn't invoke lambda
println("access lazy --> ${lazyInt.value}")

println("resetting")
resetManager.resetAll()

// creates new instance, invoking the lambda again
println("access lazy --> ${lazyInt.value}")

output shouldPrint """after declaration
  |initializing as 1
  |access lazy --> 1
  |access lazy --> 1
  |resetting
  |initializing as 2
  |access lazy --> 2
""".trimMargin()
```

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Lazy instance which can be reset.  After a reset, the next access will create a new instance.`LazyResets(resetManager: `[`ResetManager`](../-reset-manager/index.md)`, valueFactory: () -> T)` |

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `val value: T` |

### Functions

| Name | Summary |
|---|---|
| [isInitialized](is-initialized.md) | `fun isInitialized(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [reset](reset.md) | `fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
