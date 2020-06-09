

### All Types

| Name | Summary |
|---|---|
|

##### [hermit.test.junit.AutoReset](../hermit.test.junit/-auto-reset.md)

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


|

##### [hermit.test.junit.HermitExtension](../hermit.test.junit/-hermit-extension/index.md)


|

##### [hermit.test.junit.HermitJUnit5](../hermit.test.junit/-hermit-j-unit5/index.md)


