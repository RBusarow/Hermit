[hermit-coroutines](../../index.md) / [hermit.test.coroutines](../index.md) / [hermit.test.ResetManager](index.md) / [resetsScope](./resets-scope.md)

# resetsScope

`@ExperimentalCoroutinesApi fun <reified T : `[`CoroutineScope`](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/index.html)`> `[`ResetManager`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md)`.resetsScope(scope: T = when (T::class) {
    TestProvidedCoroutineScope::class  -> TestProvidedCoroutineScope()
    TestCoroutineScope::class          -> TestCoroutineScope()
    DefaultCoroutineScope::class       -> DefaultCoroutineScope()
    IOCoroutineScope::class            -> IOCoroutineScope()
    MainCoroutineScope::class          -> MainCoroutineScope()
    MainImmediateCoroutineScope::class -> MainImmediateCoroutineScope()
    UnconfinedCoroutineScope::class    -> UnconfinedCoroutineScope()
    else                               -> CoroutineScope(Job())
  } as T): `[`LazyResetsCoroutineScope`](../-lazy-resets-coroutine-scope.md)`<T>` [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-coroutines/src/main/kotlin/hermit/test/coroutines/LazyResetsCoroutineScope.kt#L28)

Binds a single instance of [CoroutineScope](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/index.html) to a [ResetManager](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md).

The same instance is re-used throughout the lifetime of the [LazyResetsCoroutineScope](../-lazy-resets-coroutine-scope.md),
since its state is entirely reset in [reset](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-resets/reset.md).

### reset behavior

If the [scope](resets-scope.md#hermit.test.coroutines$resetsScope(hermit.test.ResetManager, hermit.test.coroutines.resetsScope.T)/scope) implements [TestCoroutineScope](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/kotlinx.coroutines.test/-test-coroutine-scope/index.html), [cleanupTestCoroutines](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/kotlinx.coroutines.test/-test-coroutine-scope/cleanup-test-coroutines.html) is called.

If the [scope](resets-scope.md#hermit.test.coroutines$resetsScope(hermit.test.ResetManager, hermit.test.coroutines.resetsScope.T)/scope) does not implement [TestCoroutineScope](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/kotlinx.coroutines.test/-test-coroutine-scope/index.html) but its [coroutineContext](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html) contains a [Job](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-job/index.html),
then [Job.cancelChildren](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/cancel-children.html) is called.

If the [scope](resets-scope.md#hermit.test.coroutines$resetsScope(hermit.test.ResetManager, hermit.test.coroutines.resetsScope.T)/scope) is not a [TestCoroutineScope](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/kotlinx.coroutines.test/-test-coroutine-scope/index.html) and does not have a [Job](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-job/index.html), then [reset](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-resets/reset.md) has no effect.

``` kotlin
@ExperimentalCoroutinesApi
class ResetsScopeSample : HermitJUnit5() {

  val testScope by resetsScope(TestCoroutineScope())
  val providedScope by resetsScope<TestProvidedCoroutineScope>()
  val normalScope: CoroutineScope by resetsScope()

  @Test
  fun `resetAll should throw an exception if a TestCoroutineScope is leaking`() =
    runBlocking<Unit> {

      testScope.leak()

      shouldThrow<UncompletedCoroutinesError> {
        resetAll()
      }

      providedScope.leak()

      shouldThrow<UncompletedCoroutinesError> {
        resetAll()
      }

    }

  @Test
  fun `resetAll should cancel all child coroutines if not a test scope`() = runBlocking<Unit> {

    val leakyJob = normalScope.leak()

    resetAll()

    leakyJob.isCancelled shouldBe true
  }

  fun CoroutineScope.leak() = launch {
    while (true) {
      delay(10)
    }
  }
}
```

