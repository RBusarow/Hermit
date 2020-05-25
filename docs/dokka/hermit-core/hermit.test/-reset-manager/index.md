[hermit-core](../../index.md) / [hermit.test](../index.md) / [ResetManager](./index.md)

# ResetManager

`interface ResetManager` [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-core/src/main/kotlin/hermit/test/ResetManager.kt#L24)

Marks a class which is capable of tracking and resetting multiple [Resets](../-resets/index.md) instances.

``` kotlin
class UnsafeResetManager : ResetManager {

  private val delegates = mutableListOf<Resets>()

  override fun register(delegate: Resets) {
    delegates.add(delegate)
  }

  override fun resetAll() {
    delegates.forEach { it.reset() }
    delegates.clear()
  }

}
```

**See Also**

[ResetManager](./index.md)

### Functions

| Name | Summary |
|---|---|
| [register](register.md) | `abstract fun register(delegate: `[`Resets`](../-resets/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [resetAll](reset-all.md) | `abstract fun resetAll(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Extension Functions

| Name | Summary |
|---|---|
| [resets](../resets.md) | Lazy instance which can be reset.  After a reset, the next access will create a new instance.`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`ResetManager`](./index.md)`.resets(valueFactory: () -> T = {
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

  }): `[`LazyResets`](../-lazy-resets/index.md)`<T>` |

### Inheritors

| Name | Summary |
|---|---|
| [Hermit](../-hermit/index.md) | Factory for creating a default [ResetManager](./index.md) instance.`open class Hermit : `[`ResetManager`](./index.md) |
