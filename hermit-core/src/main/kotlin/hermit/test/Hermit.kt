package hermit.test

/**
 * Factory for creating a default [ResetManager] instance.
 *
 * All calls to [register][ResetManager.register] and [resetAll][ResetManager.resetAll] are thread-safe.
 *
 * @sample samples.DelegatingResetManagerImpl
 */
public open class Hermit(
  private val delegates: MutableCollection<Resets> = mutableListOf()
) : ResetManager {

  override fun register(delegate: Resets) {
    synchronized(delegates) {
      delegates.add(delegate)
    }
  }

  override fun resetAll() {
    synchronized(delegates) {
      delegates.forEach { it.reset() }
      delegates.clear()
    }
  }
}
