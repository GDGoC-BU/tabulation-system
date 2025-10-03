import z from 'zod'

export const awardSummarySchema = z.object({
  id: z.string(),
  name: z.string(),
  candidateLimit: z.int(),
  formula: z.string(),
})
export type AwardSummary = z.infer<typeof awardSummarySchema>

export const awardsSchema = z.array(awardSummarySchema)
export type Awards = z.infer<typeof awardsSchema>
