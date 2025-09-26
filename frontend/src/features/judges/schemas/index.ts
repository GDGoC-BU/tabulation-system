import z from 'zod'
import { zStringToDate } from '@/schemas'

export const judgeSummarySchema = z.object({
  id: z.string(),
  username: z.string(),
  isOnline: z.boolean(),
  lastSeenAt: z.union([z.null(), zStringToDate]),
  createdAt: zStringToDate,
  updatedAt: zStringToDate,
})
export type JudgeSummary = z.infer<typeof judgeSummarySchema>

export const judgesSchema = z.array(judgeSummarySchema)
export type Judges = z.infer<typeof judgesSchema>
