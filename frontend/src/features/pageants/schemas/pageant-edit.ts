import z from 'zod'

export const pageantEditSchema = z.object({
  id: z.string(),
  title: z.string()
})
