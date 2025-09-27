import z from 'zod'
import { phaseSegmentStatusValue } from '@/schemas'

export const phaseSummarySchema = z.object({
  id: z.string(),
  name: z.string(),
  sequence: z.number(),
  status: phaseSegmentStatusValue,
})
export type PhaseSummary = z.infer<typeof phaseSummarySchema>

export const phasesSchema = z.array(phaseSummarySchema)
export type Phases = z.infer<typeof phasesSchema>
