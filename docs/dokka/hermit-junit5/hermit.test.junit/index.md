[hermit-junit5](../index.md) / [hermit.test.junit](./index.md)

## Package hermit.test.junit

### Types

| Name | Summary |
|---|---|
| [AutoReset](-auto-reset.md) |

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
}<br>```
<br>`interface AutoReset : `[`ResetManager`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md) |
| [HermitExtension](-hermit-extension/index.md) | `class HermitExtension : TestInstancePostProcessor, AfterEachCallback, `[`ResetManager`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md) |
| [HermitJUnit5](-hermit-j-unit5/index.md) | `abstract class HermitJUnit5 : `[`Hermit`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-hermit/index.md)`, `[`AutoReset`](-auto-reset.md) |

### Functions

| Name | Summary |
|---|---|
| [AutoReset](-auto-reset.md) | `fun AutoReset(): `[`AutoReset`](-auto-reset.md) |
