[hermit-core](../index.md) / [hermit.test](index.md) / [LazyResets](./-lazy-resets.md)

# LazyResets

`interface LazyResets<out T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Lazy`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-lazy/index.html)`<T>, `[`Resets`](-resets/index.md) [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-core/src/main/kotlin/hermit/test/LazyResets.kt#L21)
`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> LazyResets(resetManager: `[`ResetManager`](-reset-manager/index.md)`, valueFactory: () -> T): `[`LazyResets`](./-lazy-resets.md)`<T>` [(source)](https://github.com/RBusarow/AutoReset/tree/master/hermit-core/src/main/kotlin/hermit/test/LazyResets.kt#L24)