[api](../../index.md) / [hermit.api](../index.md) / [Resets](./index.md)

# Resets

`interface Resets` [(source)](https://github.com/RBusarow/AutoReset/tree/master/api/src/main/kotlin/autoreset/api/Resets.kt#L34)

Marks any type which may be reset.

Note that some implementations of `Resettable` are lower maintenance (and therefore safer) than others.

It's reasonably safe to implement `Resettable` when all state
can be reset in a single "nuclear" option,
since future updates would automatically also be captured.

``` kotlin
/**
 * Makes a MutableMap which conforms to the Resettable interface,
 * by delegating `reset()` to the existing [clear] function.
 */
class ResettableMap<K, V> : MutableMap<K, V> by mutableMapOf(),
                            Resets {

  override fun reset() {
    clear()
  }
}
```

``` kotlin
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
```

### Functions

| Name | Summary |
|---|---|
| [reset](reset.md) | `abstract fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [LazyResets](../-lazy-resets/index.md) | Lazy instance which can be reset.  After a reset, the next access will create a new instance.`class LazyResets<out T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Lazy`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-lazy/index.html)`<T>, `[`Resets`](./index.md) |
