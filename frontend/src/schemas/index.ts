import z from 'zod'

export const zStringToDate = z.string().transform((val, ctx) => {
  const date = new Date(val)
  if (isNaN(date.getTime())) {
    ctx.addIssue({
      code: 'custom',
      message: `Invalid date: ${val}`,
    })
    return z.NEVER
  }
  return date
})

export const honorificSchema = z.enum([
  'MR',
  'MS',
  'MX',
  'MRS',
  'HON',
  'DR',
  'PROF',
])
export type Honorific = z.infer<typeof honorificSchema>

export const criteriaBreakdownSchema = z.object({
  phase: z.object({
    id: z.string(),
    name: z.string(),
  }),
  segment: z.object({
    id: z.string(),
    name: z.string(),
  }),
  criterion: z.object({
    id: z.string(),
    name: z.string(),
    maxScore: z.number(),
  }),
  averageScore: z.number(),
  scores: z.array(
    z.object({
      judge: z.object({
        id: z.string(),
        username: z.string(),
        firstName: z.string(),
        lastName: z.string(),
        honorific: honorificSchema,
      }),
      value: z.number(),
    }),
  ),
})
export type CriteriaBreakdown = z.infer<typeof criteriaBreakdownSchema>

export const pageantStatusValue = z.enum([
  'PREPARATION',
  'ONGOING',
  'FINALIZING',
  'CLOSED',
])
export type PageantStatusValue = z.infer<typeof pageantStatusValue>

export const phaseSegmentStatusValue = z.enum(['PENDING', 'ONGOING', 'CLOSED'])
export type PhaseSegmentStatusValue = z.infer<typeof phaseSegmentStatusValue>
