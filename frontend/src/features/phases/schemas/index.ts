import z from 'zod'
import { phaseSegmentStatusValue } from '@/schemas'
import {
  segmentHierarchySchema,
  segmentSummarySchema,
} from '@/features/segments/schemas'

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
  segments: z.array(z.lazy(() => segmentSummarySchema)),
})
export type PhaseDetailed = z.infer<typeof phaseDetailedSchema>

export const phaseHierarchySchema = z.object({
  id: z.string(),
  name: z.string(),
  sequence: z.number().int(),
  status: phaseSegmentStatusValue,
  segments: z.array(z.lazy(() => segmentHierarchySchema)),
})
export type PhaseHierarchy = z.infer<typeof phaseHierarchySchema>

export const phasesSchema = z.array(phaseSummarySchema)
export type Phases = z.infer<typeof phasesSchema>
