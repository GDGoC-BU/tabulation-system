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
  'ATTY',
  'PROF',
])
export type Honorific = z.infer<typeof honorificSchema>

type Json =
  | string
  | number
  | boolean
  | null
  | Array<Json>
  | { [key: string]: Json }

export const jsonSchema: z.ZodType<Json> = z.lazy(() =>
  z.union([
    z.string(),
    z.number(),
    z.boolean(),
    z.null(),
    z.array(jsonSchema),
    z.record(z.string(), jsonSchema),
  ]),
)

export const criteriaBreakdownSchema = z.object({
  /* NOTE: Move the individual fields to their respective schema fiels in features.
     But beware of the cyclic dependencies. */
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
        number: z.number(),
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
