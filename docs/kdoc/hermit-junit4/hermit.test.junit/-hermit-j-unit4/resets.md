[hermit-junit4](../../index.md) / [hermit.test.junit](../index.md) / [HermitJUnit4](index.md) / [resets](./resets.md)

# resets

`inline fun <reified T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> resets(noinline valueFactory: () -> T = {
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

    }): `[`LazyResets`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-lazy-resets/index.md)`<T>` [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-junit4/src/main/kotlin/hermit/test/junit/HermitJUnit4.kt#L24)

Convenience function which delegates to [ResetManager.resets](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/resets.md) without needing the import.

**See Also**

[resetsExtension](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/resets.md)

