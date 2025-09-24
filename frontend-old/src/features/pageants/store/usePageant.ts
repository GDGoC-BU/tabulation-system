import { create } from 'zustand'
import { PageantSummary } from '../schemas/pageant'

type PageantState = {
  pageant: PageantSummary | null
  setPageant: (pageant: PageantSummary | null) => void
}

export const usePageant = create<PageantState>((set, get) => {
  return {
    pageant: null,
    setPageant: pageant => set({ pageant })
  }
})
