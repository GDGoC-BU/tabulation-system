import z from 'zod'

export const pageantAddSchema = z.object({
  title: z.string().min(1, {
    error: 'Required'
  })
})
