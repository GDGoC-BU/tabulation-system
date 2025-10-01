import z from 'zod'
import { criterionSummarySchema } from '@/features/criteria/schemas'

export const scoreDetailedSchema = z.object({
  id: z.string(),
  value: z.int(),
  judgeId: z.string(),
  candidateId: z.string(),
  segmentId: z.string(),
  criterion: criterionSummarySchema,
})
export type ScoreDetailed = z.infer<typeof scoreDetailedSchema>
