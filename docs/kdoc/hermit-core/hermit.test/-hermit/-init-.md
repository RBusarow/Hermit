[hermit-core](../../index.md) / [hermit.test](../index.md) / [Hermit](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Hermit(delegates: `[`MutableCollection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)`<`[`Resets`](../-resets/index.md)`> = mutableListOf())`

Factory for creating a default [ResetManager](../-reset-manager/index.md) instance.

All calls to [register](../-reset-manager/register.md) and [resetAll](../-reset-manager/reset-all.md) are thread-safe.

``` kotlin
class DelegatingResetManagerImpl : ResetManager by Hermit() {

  // auto-registered with this ResetManager
  val someResettable by resets { SomeClass() }

  init {

    // initializes the someResettable instance
    someResettable

    // resets someResettable
    resetAll()
  }
}
```

