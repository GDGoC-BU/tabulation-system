import { create } from 'zustand'

type SelectedPageantIdStore = {
  selectedPageantId: string | null
  setSelectedPageantId: (id: string | null) => void
}

export const useSelectedPageantIdStore = create<SelectedPageantIdStore>(
  (set) => ({
    selectedPageantId: null,
    setSelectedPageantId: (id) => set({ selectedPageantId: id }),
  }),
)
