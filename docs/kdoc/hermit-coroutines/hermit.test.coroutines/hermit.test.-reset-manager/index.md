[hermit-coroutines](../../index.md) / [hermit.test.coroutines](../index.md) / [hermit.test.ResetManager](./index.md)

### Extensions for hermit.test.ResetManager

| Name | Summary |
|---|---|
| [resetsScope](resets-scope.md) | Binds a single instance of [CoroutineScope](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/index.html) to a [ResetManager](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md).`fun <T : `[`CoroutineScope`](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/index.html)`> `[`ResetManager`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md)`.resetsScope(scope: T = when (T::class) {
    TestProvidedCoroutineScope::class  -> TestProvidedCoroutineScope()
    TestCoroutineScope::class          -> TestCoroutineScope()
    DefaultCoroutineScope::class       -> DefaultCoroutineScope()
    IOCoroutineScope::class            -> IOCoroutineScope()
    MainCoroutineScope::class          -> MainCoroutineScope()
    MainImmediateCoroutineScope::class -> MainImmediateCoroutineScope()
    UnconfinedCoroutineScope::class    -> UnconfinedCoroutineScope()
    else                               -> CoroutineScope(Job())
  } as T): `[`LazyResetsCoroutineScope`](../-lazy-resets-coroutine-scope.md)`<T>` |
