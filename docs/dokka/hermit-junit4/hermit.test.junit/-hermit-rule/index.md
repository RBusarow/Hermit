[hermit-junit4](../../index.md) / [hermit.test.junit](../index.md) / [HermitRule](./index.md)

# HermitRule

`class HermitRule : `[`TestWatcher`](https://junit.org/junit4/javadoc/latest/org/junit/rules/TestWatcher.html)`, `[`ResetManager`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md) [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-junit4/src/main/kotlin/hermit/test/junit/HermitRule.kt#L32)

JUnit 4 [Rule](https://junit.org/junit4/javadoc/latest/org/junit/Rule.html) which automatically resets the value
of a [LazyResets](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-lazy-resets/index.md) instance after every test.

Note that the default behavior for JUnit 4
is to create an entirely new test class instance for each test,
which means this would only be of value for resetting the state of singletons.

``` kotlin
class AutoResetRuleSample {

  @JvmField
  @Rule
  val rule = HermitRule()

  /**
   * automatically has reset() called on it after each test
   */
  val someProperty by rule.resets { SomeProperty() }

  val resetManager = Hermit()

  /**
   * you can also pass in your own [ResetManager]
   */
  @JvmField
  @Rule
  val explicitRule = HermitRule(resetManager)

  @Test
  fun `some property gets reset`() {

    someProperty shouldBe false

  }
}
```

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | JUnit 4 [Rule](https://junit.org/junit4/javadoc/latest/org/junit/Rule.html) which automatically resets the value of a [LazyResets](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-lazy-resets/index.md) instance after every test.`HermitRule(delegate: `[`ResetManager`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md)` = Hermit())` |

### Functions

| Name | Summary |
|---|---|
| [finished](finished.md) | `fun finished(description: `[`Description`](https://junit.org/junit4/javadoc/latest/org/junit/runner/Description.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
