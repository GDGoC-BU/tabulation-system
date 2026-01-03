import type { PhaseHierarchy } from '@/features/phases/schemas'

export type CriterionLookupElement = {
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
export type CriterionLookup = Record<string, CriterionLookupElement>

export function generateCriterionLookup(phases: Array<PhaseHierarchy>) {
  const lookup: CriterionLookup = {}

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
}
