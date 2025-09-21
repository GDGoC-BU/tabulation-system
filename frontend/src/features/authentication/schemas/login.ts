import z from 'zod'

const loginSchema = z.object({
  username: z.string().min(5, { message: 'Short text bro' }),
  password: z.string()
})
export default loginSchema
