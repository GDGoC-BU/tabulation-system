import z from 'zod'

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

export const accountSchema = z.object({
  username: z.string(),
  token: z.string(),
  role: accountRoleSchema,
})
export type Account = z.infer<typeof accountSchema>
