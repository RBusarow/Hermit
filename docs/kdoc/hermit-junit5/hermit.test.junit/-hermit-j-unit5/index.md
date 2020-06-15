[hermit-junit5](../../index.md) / [hermit.test.junit](../index.md) / [HermitJUnit5](./index.md)

# HermitJUnit5

`abstract class HermitJUnit5 : `[`Hermit`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-hermit/index.md)`, `[`AutoReset`](../-auto-reset.md) [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-junit5/src/main/kotlin/hermit/test/junit/HermitJUnit5.kt#L7)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `HermitJUnit5(delegates: `[`MutableCollection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)`<`[`Resets`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-resets/index.md)`> = mutableListOf())` |

### Functions

| Name | Summary |
|---|---|
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
