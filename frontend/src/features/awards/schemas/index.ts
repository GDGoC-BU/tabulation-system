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

export const awardAddFormSchema = z.object({
  name: z.string().min(1, {
    error: 'Name is required',
  }),
  candidateLimit: z.int().min(1, {
    error: 'Candidate limit should be greater than 0',
  }),
  formula: z.string().min(1, {
    error: 'Formula is required',
  }),
})
export type AwardAddForm = z.infer<typeof awardAddFormSchema>
