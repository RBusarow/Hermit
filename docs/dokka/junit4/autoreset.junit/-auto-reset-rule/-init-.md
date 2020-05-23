[junit4](../../index.md) / [hermit.junit](../index.md) / [AutoResetRule](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`AutoResetRule(delegate: `[`ResetManager`](https://rbusarow.github.io/AutoReset/api/autoreset.api/-reset-manager/index.md)` = ResetManager())`

JUnit 4 [Rule](https://junit.org/junit4/javadoc/latest/org/junit/Rule.html) which automatically resets the value
of a [LazyResets](https://rbusarow.github.io/AutoReset/api/autoreset.api/-lazy-resets/index.md) instance after every test.

Note that the default behavior for JUnit 4
is to create an entirely new test class instance for each test,
which means this would only be of value for resetting the state of singletons.

``` kotlin
class AutoResetRuleSample {

  @JvmField @Rule val rule = AutoResetRule()

  /**
   * automatically has reset() called on it after each test
   */
  val someProperty by rule.resets { SomeProperty() }

  val resetManager = ResetManager()

  /**
   * you can also pass in your own [ResetManager]
   */
  @JvmField @Rule val explicitRule = AutoResetRule(resetManager)

  @Test
  fun `some property gets reset`() {

    someProperty shouldBe false

  }
}
```

