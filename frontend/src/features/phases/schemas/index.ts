import z from 'zod'
import { phaseSegmentStatusValue } from '@/schemas'
import { segmentSummarySchema } from '@/features/segments/schemas'

export const phaseSummarySchema = z.object({
  id: z.string(),
  name: z.string(),
  sequence: z.number(),
  status: phaseSegmentStatusValue,
})
export type PhaseSummary = z.infer<typeof phaseSummarySchema>

export const phaseDetailedSchema = z.object({
  id: z.string(),
  name: z.string(),
  sequence: z.number().int(),
  status: phaseSegmentStatusValue,
  segments: z.array(segmentSummarySchema),
})
export type PhaseDetailedDTO = z.infer<typeof phaseDetailedSchema>

export const phasesSchema = z.array(phaseSummarySchema)
export type Phases = z.infer<typeof phasesSchema>
