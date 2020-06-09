[hermit-core](../../index.md) / [hermit.test](../index.md) / [Hermit](./index.md)

# Hermit

`open class Hermit : `[`ResetManager`](../-reset-manager/index.md) [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-core/src/main/kotlin/hermit/test/Hermit.kt#L10)

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

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Factory for creating a default [ResetManager](../-reset-manager/index.md) instance.`Hermit(delegates: `[`MutableCollection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)`<`[`Resets`](../-resets/index.md)`> = mutableListOf())` |

### Functions

| Name | Summary |
|---|---|
| [register](register.md) | `open fun register(delegate: `[`Resets`](../-resets/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [resetAll](reset-all.md) | `open fun resetAll(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Extension Functions

| Name | Summary |
|---|---|
| [resets](../resets.md) | Lazy instance which can be reset.  After a reset, the next access will create a new instance.`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`ResetManager`](../-reset-manager/index.md)`.resets(valueFactory: () -> T = {
    val clazz = T::class

    try {
      clazz.java.newInstance()
    } catch (illegal: IllegalAccessException) {

      val obj = clazz.objectInstance

      if (obj != null && obj is Resets) {
        obj
      } else {
        throw LazyResetDelegateObjectException(clazz)
      }
    } catch (abstract: InstantiationException) {

      when {
        clazz.java.isInterface -> throw LazyResetDelegateInterfaceException(clazz)
        clazz.isAbstract -> throw LazyResetDelegateAbstractException(clazz)
        else -> throw LazyResetDelegateNonDefaultConstructorException(clazz)
      }
    }

  }): `[`LazyResets`](../-lazy-resets.md)`<T>` |
