import { create } from 'zustand'

type BlocklyStore = {
  criterionDropdownOptions: Array<[string, string] | 'separator'> | null
  setCriterionDropdownOptions: (
    options: Array<[string, string] | 'separator'>,
  ) => void
}

export const useBlocklyStore = create<BlocklyStore>((set) => ({
  criterionDropdownOptions: null,
  setCriterionDropdownOptions: (options) =>
    set({ criterionDropdownOptions: options }),
}))
