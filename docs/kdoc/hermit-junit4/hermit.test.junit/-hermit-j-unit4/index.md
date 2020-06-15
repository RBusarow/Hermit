[hermit-junit4](../../index.md) / [hermit.test.junit](../index.md) / [HermitJUnit4](./index.md)

# HermitJUnit4

`abstract class HermitJUnit4 : `[`ResetManager`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md) [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-junit4/src/main/kotlin/hermit/test/junit/HermitJUnit4.kt#L7)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `HermitJUnit4(resetManager: `[`ResetManager`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md)`)` |

### Functions

| Name | Summary |
|---|---|
| [resetAll](reset-all.md) | `open fun resetAll(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [resets](resets.md) | Convenience function which delegates to [ResetManager.resets](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/resets.md) without needing the import.`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> resets(valueFactory: () -> T = {
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
          clazz.isAbstract       -> throw LazyResetDelegateAbstractException(clazz)
          else                   -> throw LazyResetDelegateNonDefaultConstructorException(clazz)
        }
      }

    }): `[`LazyResets`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-lazy-resets/index.md)`<T>` |
