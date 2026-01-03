import { create } from 'zustand'
import type { CriterionLookup } from '@/features/criteria/lib/generate-criterion-lookup'

type CriterionDropdown = {
  criterionLookup: CriterionLookup | null
  setCriterionLookup: (lookup: CriterionLookup) => void
}

export const useCriterionDropdownStore = create<CriterionDropdown>((set) => ({
  criterionLookup: null,
  setCriterionLookup: (lookup) => set({ criterionLookup: lookup }),
}))
