[hermit-junit5](../index.md) / [hermit.test.junit](index.md) / [AutoReset](./-auto-reset.md)

# AutoReset

`@ExtendWith([NormalClass(value=hermit/test/junit/AutoResetExtension)]) interface AutoReset : `[`ResetManager`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md) [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-junit5/src/main/kotlin/hermit/test/junit/AutoReset.kt#L26)

``` kotlin
/**
 * The JUnit 5 extension is automatically applied,
 * and dependencies are provided via `by AutoReset()`.
 *
 * The test lifecycle is automatically managed.
 */
class AutoResetClassDelegationSample : AutoReset by AutoReset() {

  /**
   * Automatically reset between each test, including nested class tests.
   */
  val someDao: SomeDao by resets { SomeDao() }

  @Test
  fun `test one`() {

    val thing = Thing()

    someDao.insertThing(thing)

    someDao.getAllThings() shouldBe listOf(thing)
  }

  @Test
  fun `test two`() {

    // This test passes because someDao has been reset and is a different instance
    someDao.getAllThings() shouldBe listOf()
  }
}
```

`fun AutoReset(): `[`AutoReset`](./-auto-reset.md) [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-junit5/src/main/kotlin/hermit/test/junit/AutoReset.kt#L28)