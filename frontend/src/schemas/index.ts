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

export const pageantStatusValue = z.enum([
  'PREPARATION',
  'ONGOING',
  'FINALIZING',
  'CLOSED',
])
export type PageantStatusValue = z.infer<typeof pageantStatusValue>

export const phaseSegmentStatusValue = z.enum(['PENDING', 'ONGOING', 'CLOSED'])
export type PhaseSegmentStatusValue = z.infer<typeof phaseSegmentStatusValue>
