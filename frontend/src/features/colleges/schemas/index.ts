import z from 'zod'

export const collegeSummarySchema = z.object({
  id: z.string(),
  code: z.string(),
  name: z.string(),
})
export type CollegeSummary = z.infer<typeof collegeSummarySchema>

export const collegesSchema = z.array(collegeSummarySchema)
export type Colleges = z.infer<typeof collegesSchema>
