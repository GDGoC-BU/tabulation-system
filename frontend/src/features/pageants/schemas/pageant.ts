import { zStringToDate } from '@/schemas'
import z from 'zod'

export const pageantStatusSchema = z.object({
  value: z.string(),
  color: z.string()
})
export type PageantStatus = z.infer<typeof pageantStatusSchema>

export const pageantSummarySchema = z.object({
  id: z.string(),
  title: z.string(),
  status: pageantStatusSchema,
  startedAt: z.union([z.null(), zStringToDate]),
  endedAt: z.union([z.null(), zStringToDate]),
  createdAt: zStringToDate,
  updatedAt: zStringToDate
})
export type PageantSummary = z.infer<typeof pageantSummarySchema>

export const pageantsSchema = z.array(pageantSummarySchema)
