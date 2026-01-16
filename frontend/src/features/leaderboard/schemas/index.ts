import z from 'zod'
import { candidateSummarySchema } from '@/features/candidates/schemas'
import { criteriaBreakdownSchema, zStringToDate } from '@/schemas'
import { formulaSchema } from '@/features/formula/schemas'

export const leaderboardEntrySummarySchema = z.object({
  id: z.string(),
  candidate: candidateSummarySchema,
  rank: z.int(),
  score: z.number(),
  isOverridden: z.boolean(),
  overrideReason: z.union([z.null(), z.string()]),
  isTied: z.boolean(),
  isSelected: z.boolean(),
  criteriaBreakdown: z.union([z.null(), z.array(criteriaBreakdownSchema)]),
})
export type LeaderboardEntry = z.infer<typeof leaderboardEntrySummarySchema>

export const leaderboardSummarySchema = z.object({
  formula: formulaSchema,
  selectionCount: z.int(),
  lastCalculatedAt: zStringToDate,
})
export type LeaderboardSummary = z.infer<typeof leaderboardSummarySchema>

export const leaderboardDetailedSchema = z.object({
  id: z.string(),
  formula: formulaSchema,
  selectionCount: z.int(),
  lastCalculatedAt: zStringToDate,
  entries: z.array(leaderboardEntrySummarySchema),
})
export type LeaderboardDetailed = z.infer<typeof leaderboardDetailedSchema>

export const leaderboardAddFormSchema = z.object({
  formula: formulaSchema,
  selectionCount: z
    .int()
    .gt(1, { message: 'selectionCount must be greater than 1' }),
})
export type LeaderboardAddForm = z.infer<typeof leaderboardAddFormSchema>

export const leaderboardEditFormSchema = z.object({
  formula: formulaSchema,
  selectionCount: z
    .int()
    .gt(1, { message: 'selectionCount must be greater than 1' }),
})
export type LeaderboardEditForm = z.infer<typeof leaderboardEditFormSchema>
