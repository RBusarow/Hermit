[hermit-mockk](../../index.md) / [hermit.test.mockk](../index.md) / [ResetsMockK](./index.md)

# ResetsMockK

`class ResetsMockK<out T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Lazy`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-lazy/index.html)`<T>, `[`Resets`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-resets/index.md) [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-mockk/src/main/kotlin/hermit/test/mockk/ResetsMockK.kt#L38)

Lazy MockK object for which [reset](reset.md) will call [clearMocks](#).

# Lifecycle

## On initialization

* creates a MockK instance
* *if `block` was specified* sets all answers from the `every { ... }` `block` argument

## On first access after reset

* registers the [mock](#) with the [resetManager](#)

## On repeated access

* returns the same [mock](#) instance and does nothing with `block`

## On reset

* calls [clearMocks(mock)](#) and eagerly invokes `block` again to reset the state to the initial one

**Implementation Note**

This behavior of eagerly invoking the answers `block` is atypical for this library.  It is necessary
because otherwise, it is likely that the first access of the [mock](#) instance would be inside an [every](#) block.
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

### Parameters

`resetManager` - The [ResetManager](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md) which is shared by all mocks and other resettable fields in the test.

`mock` - The mock instance exposed by the Lazy delegate.

`clearPolicy` - Describes the parameters passed via [clearMocks](#) when invoking [reset](reset.md)

`block` - The answers ( `every { ... } returns ...` ) applied after a reset.

### Types

| Name | Summary |
|---|---|
| [ClearPolicy](-clear-policy/index.md) | `data class ClearPolicy` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Lazy MockK object for which [reset](reset.md) will call [clearMocks](#).`ResetsMockK(resetManager: `[`ResetManager`](https://rbusarow.github.io/Hermit/hermit-core/hermit.test/-reset-manager/index.md)`, mock: T, clearPolicy: ClearPolicy = ClearPolicy(), block: T.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = {})` |

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `val value: T` |

### Functions

| Name | Summary |
|---|---|
| [isInitialized](is-initialized.md) | `fun isInitialized(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [reset](reset.md) | `fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
