import { zStringToDate } from '@/schemas'
import z from 'zod'

export const pageantSummarySchema = z.object({
  id: z.string(),
  title: z.string(),
  status: z.string(),
  startedAt: z.union([z.null(), zStringToDate]),
  endedAt: z.union([z.null(), zStringToDate]),
  createdAt: zStringToDate,
  updatedAt: zStringToDate
})
export type PageantSummary = z.infer<typeof pageantSummarySchema>

export const pageantsSchema = z.array(pageantSummarySchema)
