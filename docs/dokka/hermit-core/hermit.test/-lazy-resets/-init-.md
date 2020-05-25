[hermit-core](../../index.md) / [hermit.test](../index.md) / [LazyResets](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`LazyResets(resetManager: `[`ResetManager`](../-reset-manager/index.md)`, valueFactory: () -> T)`

Lazy instance which can be reset.  After a reset, the next access will create a new instance.

Each time a new instance is created, it is registered with the [ResetManager](../-reset-manager/index.md) and will be reset with the next [ResetManager.resetAll](../-reset-manager/reset-all.md).

``` kotlin
var instanceNumber = 0

val resetManager = Hermit()

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

