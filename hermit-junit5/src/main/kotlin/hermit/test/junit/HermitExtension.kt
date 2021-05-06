package hermit.test.junit

import hermit.test.Hermit
import hermit.test.ResetManager
import hermit.test.Resets
import kotlinx.atomicfu.atomic
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestInstancePostProcessor

public class HermitExtension(
  resetManager: ResetManager = Hermit()
) : TestInstancePostProcessor,
  AfterEachCallback,
  ResetManager {

  private var tempDelegates: MutableList<Resets>? = mutableListOf()

  private val topInstanceProcessed = atomic(false)

  private var delegate: ResetManager = resetManager
    set(new) {
      field = new
      tempDelegates = null
    }

  override fun register(delegate: Resets) {
    if (topInstanceProcessed.value) {
      this.delegate.register(delegate)
    } else {
      synchronized(this) {
        tempDelegates?.add(delegate)
      }
    }
  }

  override fun resetAll() {
    delegate.resetAll()
    synchronized(this) {
      tempDelegates?.forEach { it.reset() }
      tempDelegates?.clear()
    }
  }

  override fun postProcessTestInstance(testInstance: Any?, context: ExtensionContext?) {
    /*
    Nested classes also trigger `postProcessTestInstance`,
    so add a check to only set the instance for the first (top-most) instance.
     */
    if (!topInstanceProcessed.value) {
      (testInstance as? ResetManager)?.let {
        delegate = it
      }
    }

    topInstanceProcessed.compareAndSet(expect = false, update = true)
  }

  override fun afterEach(context: ExtensionContext?) {
    resetAll()
  }
}
