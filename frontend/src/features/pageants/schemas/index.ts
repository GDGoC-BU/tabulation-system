import z from 'zod'
import { zStringToDate } from '@/schemas'

const pageantStatusSchema = z.object({
  value: z.string(),
  color: z.string(),
})

export const pageantSummarySchema = z.object({
  id: z.string(),
  title: z.string(),
  status: pageantStatusSchema,
  startedAt: z.union([z.null(), zStringToDate]),
  endedAt: z.union([z.null(), zStringToDate]),
  createdAt: zStringToDate,
  updatedAt: zStringToDate,
})
export type PageantSummary = z.infer<typeof pageantSummarySchema>

export const pageantsSchema = z.array(pageantSummarySchema)
export type Pageants = z.infer<typeof pageantsSchema>

export const pageantAddSchema = z.object({
  title: z.string().min(1, {
    error: 'Required',
  }),
})
export type PageantAddParameters = z.infer<typeof pageantAddSchema>

export const pageantEditSchema = z.object({
  id: z.string().min(1, {
    error: 'Required',
  }),
  title: z.string().min(1, {
    error: 'Required',
  }),
})
export type PageantEditParameters = z.infer<typeof pageantEditSchema>

export const pageantDeleteSchema = z.object({
  id: z.string().min(1, {
    error: 'Required',
  }),
})
export type PageantDeleteParameters = z.infer<typeof pageantDeleteSchema>
