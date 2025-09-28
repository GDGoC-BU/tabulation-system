import z from 'zod'
import { zStringToDate } from '@/schemas'

export const loginSchema = z.object({
  username: z.string().min(1, {
    error: 'Required',
  }),
  password: z.string().min(1, {
    error: 'Required',
  }),
})
export type LoginParameters = z.infer<typeof loginSchema>

export const accountRoleSchema = z.enum(['ADMIN', 'JUDGE', 'ORGANIZER'])
export type AccountRole = z.infer<typeof accountRoleSchema>

export const accountStoreSchema = z.object({
  username: z.string(),
  token: z.string(),
  role: accountRoleSchema,
})
export type AccountStore = z.infer<typeof accountStoreSchema>

export const accountSummarySchema = z.object({
  id: z.string(),
  username: z.string(),
  isOnline: z.boolean(),
  lastSeenAt: z.union([z.null(), zStringToDate]),
})
export type AccountSummary = z.infer<typeof accountSummarySchema>
