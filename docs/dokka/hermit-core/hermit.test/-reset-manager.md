[hermit-core](../index.md) / [hermit.test](index.md) / [ResetManager](./-reset-manager.md)

# ResetManager

`fun ResetManager(): `[`ResetManager`](-reset-manager/index.md) [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-core/src/main/kotlin/hermit/test/ResetManager.kt#L37)

Factory for creating a default [ResetManager](-reset-manager/index.md) instance.

All calls to [register](-reset-manager/register.md) and [resetAll](-reset-manager/reset-all.md) are thread-safe.

``` kotlin
class DelegatingResetManagerImpl : ResetManager by ResetManager() {

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

