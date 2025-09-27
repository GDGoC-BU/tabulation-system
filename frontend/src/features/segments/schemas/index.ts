import z from 'zod'
import { phaseSegmentStatusValue } from '@/schemas'
import { phaseSummarySchema } from '@/features/phases/schemas'

export const segmentSummarySchema = z.object({
  id: z.string(),
  name: z.string(),
  sequence: z.number(),
  status: phaseSegmentStatusValue,
  phase: phaseSummarySchema,
})
export type segmentSummary = z.infer<typeof segmentSummarySchema>

export const segmentsSchema = z.array(segmentSummarySchema)
export type Segments = z.infer<typeof segmentsSchema>
