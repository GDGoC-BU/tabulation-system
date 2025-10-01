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

export const scoresSchema = z.array(scoreDetailedSchema)
export type Scores = z.infer<typeof scoresSchema>

export const scoreEditFormSchema = z.object({
  value: z.int().min(0, {
    error: 'Must be equal or greater than 0',
  }),
})
export type ScoreEditFormSchema = z.infer<typeof scoreEditFormSchema>
