import z from 'zod'
import { candidateSummarySchema } from '@/features/candidates/schemas'
import { criteriaBreakdownSchema } from '@/schemas'

export const awardLeaderboardSummarySchema = z.object({
  id: z.string(),
  candidate: candidateSummarySchema,
  score: z.number(),
  criteriaBreakdown: z.array(criteriaBreakdownSchema),
})
export type AwardLeaderboardSummary = z.infer<
  typeof awardLeaderboardSummarySchema
>

export const awardLeaderboardsSchema = z.array(awardLeaderboardSummarySchema)
export type AwardLeaderboards = z.infer<typeof awardLeaderboardsSchema>

export const awardSummarySchema = z.object({
  id: z.string(),
  name: z.string(),
  candidateLimit: z.int(),
  formula: z.string(),
})
export type AwardSummary = z.infer<typeof awardSummarySchema>

export const awardDetailedSchema = z.object({
  id: z.string(),
  name: z.string(),
  candidateLimit: z.int(),
  formula: z.string(),
  leaderboard: awardLeaderboardsSchema,
})
export type AwardDetailed = z.infer<typeof awardDetailedSchema>

export const awardsSchema = z.array(awardSummarySchema)
export type Awards = z.infer<typeof awardsSchema>

export const awardAddFormSchema = z.object({
  name: z.string().min(1, {
    error: 'Name is required',
  }),
  candidateLimit: z.coerce
    .number({
      message: 'Invalid value! Only positive numbers are allowed',
    })
    .min(1, {
      message: 'Enter how many candidates will get this award',
    }),
  formula: z.string().min(1, {
    error: 'Formula is required',
  }),
})
export type AwardAddForm = z.infer<typeof awardAddFormSchema>

export const awardEditFormSchema = z.object({
  id: z.string().min(1, {
    error: 'Id is missing in award edit payload',
  }),
  name: z.string().min(1, {
    error: 'Name is required',
  }),
  candidateLimit: z.coerce
    .number({
      message: 'Invalid value! Only positive numbers are allowed',
    })
    .min(1, {
      message: 'Enter how many candidates will get this award',
    }),
  formula: z.string().min(1, {
    error: 'Formula is required',
  }),
})
export type AwardEditForm = z.infer<typeof awardEditFormSchema>
