[hermit-core](../../index.md) / [hermit.test](../index.md) / [LazyResetDelegateException](./index.md)

# LazyResetDelegateException

`abstract class LazyResetDelegateException : `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html) [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-core/src/main/kotlin/hermit/test/LazyResets.kt#L136)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `LazyResetDelegateException(problemClass: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>, message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Inheritors

| Name | Summary |
|---|---|
| [LazyResetDelegateAbstractException](../-lazy-reset-delegate-abstract-exception/index.md) | Abstract classes cannot be used with a 'by resets' delegate since they cannot be instantiated.`class LazyResetDelegateAbstractException : `[`LazyResetDelegateException`](./index.md) |
| [LazyResetDelegateInterfaceException](../-lazy-reset-delegate-interface-exception/index.md) | Interfaces cannot be used with a 'by resetss' delegate since they cannot be instantiated.`class LazyResetDelegateInterfaceException : `[`LazyResetDelegateException`](./index.md) |
| [LazyResetDelegateNonDefaultConstructorException](../-lazy-reset-delegate-non-default-constructor-exception/index.md) | Classes without a default constructor cannot be used with a 'by resets' delegate.`class LazyResetDelegateNonDefaultConstructorException : `[`LazyResetDelegateException`](./index.md) |
| [LazyResetDelegateObjectException](../-lazy-reset-delegate-object-exception/index.md) | Objects may not be used with a 'by resets' delegate unless they implement the LazyReset interface.`class LazyResetDelegateObjectException : `[`LazyResetDelegateException`](./index.md) |
