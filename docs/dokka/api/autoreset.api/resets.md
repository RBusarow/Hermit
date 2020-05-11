[api](../index.md) / [autoreset.api](index.md) / [resets](./resets.md)

# resets

`inline fun <reified T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`ResetManager`](-reset-manager/index.md)`.resets(noinline valueFactory: () -> T = {
    val clazz = T::class

    try {
      clazz.java.newInstance()
    } catch (illegal: IllegalAccessException) {

      val obj = clazz.objectInstance

      if (obj != null && obj is Resets) {
        obj
      } else {
        throw LazyResetDelegateObjectException(clazz)
      }
    } catch (abstract: InstantiationException) {

      when {
        clazz.java.isInterface -> throw LazyResetDelegateInterfaceException(clazz)
        clazz.isAbstract       -> throw LazyResetDelegateAbstractException(clazz)
        else                   -> throw LazyResetDelegateNonDefaultConstructorException(clazz)
      }
    }

  }): `[`LazyResets`](-lazy-resets/index.md)`<T>` [(source)](https://github.com/RBusarow/AutoReset/tree/master/api/src/main/kotlin/autoreset/api/LazyResets.kt#L65)

Lazy instance which can be reset.  After a reset, the next access will create a new instance.

Each time a new instance is created, it is registered with the [ResetManager](-reset-manager/index.md) and will be reset with the next [ResetManager.resetAll](-reset-manager/reset-all.md).

If the type being created can be initialized with a default constructor,
this function can be invoked without a [valueFactory](resets.md#autoreset.api$resets(autoreset.api.ResetManager, kotlin.Function0((autoreset.api.resets.T)))/valueFactory) argument.

If the type being created is an interface, abstract class, or does not have a default constructor,
then a factory must be specified.

If the type being created is a Kotlin object, then it must implement the [Resets](-resets/index.md) interface

``` kotlin
var instanceNumber = 0

val resetManager = ResetManager()

val lazyInt = LazyResets(resetManager) {

  val newValue = ++instanceNumber

  println("initializing as $newValue")

  newValue
}

println("after declaration")

// creates new instance, invoking the lambda
println("access lazy --> ${lazyInt.value}")
// instance already exists, so uses the same one and doesn't invoke lambda
println("access lazy --> ${lazyInt.value}")

println("resetting")
resetManager.resetAll()

// creates new instance, invoking the lambda again
println("access lazy --> ${lazyInt.value}")

output shouldPrint """after declaration
  |initializing as 1
  |access lazy --> 1
  |access lazy --> 1
  |resetting
  |initializing as 2
  |access lazy --> 2
""".trimMargin()
```

``` kotlin
class SomeImplementation : ResetManager by ResetManager() {

  var instanceNumber = 0

  val lazyInt by resets {

    val newValue = ++instanceNumber

    println("initializing as $newValue")

    newValue
  }

}

val impl = SomeImplementation()

println("after declaration")

// creates new instance, invoking the lambda
println("access lazy --> ${impl.lazyInt}")
// instance already exists, so uses the same one and doesn't invoke lambda
println("access lazy --> ${impl.lazyInt}")

println("resetting")
impl.resetAll()

// creates new instance, invoking the lambda again
println("access lazy --> ${impl.lazyInt}")

output shouldPrint """after declaration
  |initializing as 1
  |access lazy --> 1
  |access lazy --> 1
  |resetting
  |initializing as 2
  |access lazy --> 2
""".trimMargin()
```

