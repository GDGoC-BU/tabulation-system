import { useMemo } from 'react'
import type { PhaseHierarchy } from '@/features/phases/schemas'

export type FormulaCriterion = {
  phase: {
    id: string
    name: string
    sequence: number
  }
  segment: {
    id: string
    name: string
    sequence: number
  }
  criterion: {
    id: string
    name: string
  }
}
export type FormulaCriterionLookup = Record<string, FormulaCriterion>

export default function useFormulaCriterionLookup(
  phases: Array<PhaseHierarchy>,
) {
  return useMemo(() => {
    const lookup: FormulaCriterionLookup = {}

    phases.forEach((phase) => {
      phase.segments.forEach((segment) => {
        segment.criteria.forEach((criterion) => {
          lookup[criterion.id] = {
            phase: {
              id: phase.id,
              name: phase.name,
              sequence: phase.sequence,
            },
            segment: {
              id: segment.id,
              name: segment.name,
              sequence: segment.sequence,
            },
            criterion: {
              id: criterion.id,
              name: criterion.name,
            },
          }
        })
      })
    })

    return lookup
  }, [phases])
}
