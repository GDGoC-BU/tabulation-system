import { create } from 'zustand'

type SelectedPageantIdStore = {
  selectedPageantId: string | null
  setSelectedPageantId: (id: string | null) => void
}

export const useSelectedPageantId = create<SelectedPageantIdStore>((set) => ({
  selectedPageantId: null,
  setSelectedPageantId: (id) => set({ selectedPageantId: id }),
}))
