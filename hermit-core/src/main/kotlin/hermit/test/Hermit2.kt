package hermit.test

import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass

public abstract class Hermit2<E : Any>(
  private val environmentClass: KClass<E>
) {

  public fun test(
    action: suspend E.() -> Unit
  ) {

    val instance = environmentClass.constructors.first().call()

    runBlocking {
      instance.action()
    }
  }
}
