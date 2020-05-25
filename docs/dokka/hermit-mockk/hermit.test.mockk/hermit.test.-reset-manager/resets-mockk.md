[hermit-mockk](../../index.md) / [hermit.test.mockk](../index.md) / [hermit.test.ResetManager](index.md) / [resetsMockk](./resets-mockk.md)

# resetsMockk

`inline fun <reified T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`ResetManager`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md)`.resetsMockk(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, relaxed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, vararg moreInterfaces: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>, relaxUnitFun: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, clearPolicy: ClearPolicy = ResetsMockK.ClearPolicy(), noinline block: T.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = {}): `[`ResetsMockK`](../-resets-mock-k/index.md)`<T>` [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-mockk/src/main/kotlin/hermit/test/mockk/ResetsMockK.kt#L113)

Creates a [ResetsMockK](../-resets-mock-k/index.md) object for which [reset](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-resets/reset.md) will call [clearMocks](#).

# Lifecycle

## On initialization

* creates a MockK instance
* *if `block` was specified* sets all answers from the `every { ... }` `block` argument

## On first access after reset

* registers the mock with the [ResetManager](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md) receiver

## On repeated access

* returns the same mock instance and does nothing with `block`

## On reset

* calls [clearMocks(mock)](#) and eagerly invokes `block` again to reset the state to the initial one

**Implementation Note**

This behavior of eagerly invoking the answers `block` is atypical for this library.  It is necessary
because otherwise, it is likely that the first access of the mock instance would be inside an [every](#) block.
Attempting to invoke [every](#) while already inside [every](#) causes a [MockKException](#).

``` kotlin
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimpleTest : HermitJUnit5() {

  val car: Car by resetsMockk()

  @Test
  @Order(1)
  fun `test-specific answers should be applied`() {

    every { car.manufacturer } returns "Tesla"

    car.manufacturer shouldBe "Tesla"
  }

  @Test
  @Order(2)
  fun `mocks are strict by default`() {

    shouldThrow<MockKException> { car.manufacturer shouldBe "Honda" }
  }

  @Test
  @Order(3)
  fun `recorded calls are reset for each test`() {

    verify(exactly = 0) { car.manufacturer }
  }

}
```

``` kotlin
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ComplexTest : HermitJUnit5() {

  val car: Car by resetsMockk(
    name = "Civic",
    relaxed = true,
    clearPolicy = ResetsMockK.ClearPolicy(recordedCalls = false)
  ) {
    every { manufacturer } returns "Honda"
  }

  @Test
  @Order(1)
  fun `test-specific answers should be applied`() {

    every { car.manufacturer } returns "Tesla"

    car.manufacturer shouldBe "Tesla"
  }

  @Test
  @Order(2)
  fun `answers from init block should be applied by default`() {

    car.manufacturer shouldBe "Honda"
  }

  @Test
  @Order(3)
  fun `recorded calls should not be reset for each test`() {

    verify(exactly = 2) { car.manufacturer }

    car.nunmberOfWheels() shouldBe 0 // relaxed return value
  }

}
```

**See Also**

[mockk](#)

[ResetsMockK](../-resets-mock-k/index.md)

