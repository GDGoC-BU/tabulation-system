import z from 'zod'
import { phaseSegmentStatusValue } from '@/schemas'
import { phaseSummarySchema } from '@/features/phases/schemas'
import {
  criterionHierarchySchema,
  criterionSummarySchema,
} from '@/features/criteria/schemas'
import { candidateDetailedSchema } from '@/features/candidates/schemas'

export const segmentSummarySchema = z.object({
  id: z.string(),
  name: z.string(),
  sequence: z.number(),
  status: phaseSegmentStatusValue,
  phase: z.lazy(() => {
    /* Circular import patch! segments/schema imports from phases/schema.
       Code below doesn't load the schema properly. This somehow solves the issue */
    /* const { phaseSummarySchema } = require('@/features/phases/schemas') */
    return phaseSummarySchema
  }),
})
export type segmentSummary = z.infer<typeof segmentSummarySchema>

export const segmentDetailedSchema = z.object({
  id: z.string(),
  name: z.string(),
  sequence: z.number(),
  status: phaseSegmentStatusValue,
  phase: z.lazy(() => {
    return phaseSummarySchema
  }),
  criteria: z.array(criterionSummarySchema),
  qualifiedCandidates: z.array(candidateDetailedSchema),
})
export type SegmentDetailed = z.infer<typeof segmentDetailedSchema>

export const segmentHierarchySchema = z.object({
  id: z.string(),
  name: z.string(),
  sequence: z.number(),
  status: phaseSegmentStatusValue,
  criteria: z.array(criterionHierarchySchema),
})
export type SegmentHierarchy = z.infer<typeof segmentHierarchySchema>

export const segmentsSchema = z.array(segmentSummarySchema)
export type Segments = z.infer<typeof segmentsSchema>
