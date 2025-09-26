import type { StoreApi } from 'zustand'

// Type for Zustand store with persist
type ZustandPersistedStore<T> = StoreApi<T> & {
  persist: {
    hasHydrated: () => boolean
    onFinishHydration: (callback: () => void) => () => void
  }
}

/* Utility function to wait for persisted zustand store to load from storage */
export const waitForStoreHydration = async <T>(
  store: ZustandPersistedStore<T>,
) => {
  return new Promise<void>((resolve) => {
    if (store.persist.hasHydrated()) {
      return resolve()
    }

    const unsub = store.persist.onFinishHydration(() => {
      resolve()
      unsub()
    })
  })
}
