[api](../index.md) / [hermit.api](./index.md)

## Package hermit.api

### Types

| Name | Summary |
|---|---|
| [LazyResets](-lazy-resets/index.md) | Lazy instance which can be reset.  After a reset, the next access will create a new instance.`class LazyResets<out T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Lazy`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-lazy/index.html)`<T>, `[`Resets`](-resets/index.md) |
| [ResetManager](-reset-manager/index.md) | Marks a class which is capable of tracking and resetting multiple [Resets](-resets/index.md) instances.`interface ResetManager` |
| [Resets](-resets/index.md) | Marks any type which may be reset.`interface Resets` |

### Exceptions

| Name | Summary |
|---|---|
| [LazyResetDelegateAbstractException](-lazy-reset-delegate-abstract-exception/index.md) | Abstract classes cannot be used with a 'by resets' delegate since they cannot be instantiated.`class LazyResetDelegateAbstractException : `[`LazyResetDelegateException`](-lazy-reset-delegate-exception/index.md) |
| [LazyResetDelegateException](-lazy-reset-delegate-exception/index.md) | `abstract class LazyResetDelegateException : `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html) |
| [LazyResetDelegateInterfaceException](-lazy-reset-delegate-interface-exception/index.md) | Interfaces cannot be used with a 'by resetss' delegate since they cannot be instantiated.`class LazyResetDelegateInterfaceException : `[`LazyResetDelegateException`](-lazy-reset-delegate-exception/index.md) |
| [LazyResetDelegateNonDefaultConstructorException](-lazy-reset-delegate-non-default-constructor-exception/index.md) | Classes without a default constructor cannot be used with a 'by resets' delegate.`class LazyResetDelegateNonDefaultConstructorException : `[`LazyResetDelegateException`](-lazy-reset-delegate-exception/index.md) |
| [LazyResetDelegateObjectException](-lazy-reset-delegate-object-exception/index.md) | Objects may not be used with a 'by resets' delegate unless they implement the LazyReset interface.`class LazyResetDelegateObjectException : `[`LazyResetDelegateException`](-lazy-reset-delegate-exception/index.md) |

### Functions

| Name | Summary |
|---|---|
| [ResetManager](-reset-manager.md) | Factory for creating a default [ResetManager](-reset-manager/index.md) instance.`fun ResetManager(): `[`ResetManager`](-reset-manager/index.md) |
| [resets](resets.md) | Lazy instance which can be reset.  After a reset, the next access will create a new instance.`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`ResetManager`](-reset-manager/index.md)`.resets(valueFactory: () -> T = {
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

  }): `[`LazyResets`](-lazy-resets/index.md)`<T>` |
