import { create } from 'zustand'
import type { CriterionLookup } from '@/features/criteria/lib/generate-criterion-lookup'

type BlocklyStore = {
  criterionLookup: CriterionLookup | null
  setCriterionLookup: (lookup: CriterionLookup) => void
}

export const useBlocklyStore = create<BlocklyStore>((set) => ({
  criterionLookup: null,
  setCriterionLookup: (lookup) => set({ criterionLookup: lookup }),
}))
