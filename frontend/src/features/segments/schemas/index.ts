import z from 'zod'
import { phaseSegmentStatusValue } from '@/schemas'
import { phaseSummarySchema } from '@/features/phases/schemas'

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

export const segmentsSchema = z.array(segmentSummarySchema)
export type Segments = z.infer<typeof segmentsSchema>
