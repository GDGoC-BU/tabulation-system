import { create } from 'zustand'
import type { PageantSummary } from '../schemas'

type SelectedPageantStore = {
  pageant: PageantSummary | null
  setPageant: (pageant: PageantSummary | null) => void
}

export const useSelectedPageant = create<SelectedPageantStore>((set, get) => {
  return {
    pageant: null,
    setPageant: (pageant) => set({ pageant }),
  }
})
