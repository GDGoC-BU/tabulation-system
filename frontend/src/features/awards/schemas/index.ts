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
