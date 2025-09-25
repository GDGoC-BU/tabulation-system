import z from 'zod'

const loginSchema = z.object({
  username: z.string().min(1, {
    error: 'Required',
  }),
  password: z.string().min(1, {
    error: 'Required',
  }),
})
export default loginSchema

export const accountSchema = z.object({
  username: z.string(),
  token: z.string(),
  role: z.string(),
})
export type Account = z.infer<typeof accountSchema>
