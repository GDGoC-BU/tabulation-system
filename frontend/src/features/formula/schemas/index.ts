import z from 'zod'
import { jsonSchema } from '@/schemas'

export const formulaSchema = z.object({
  text: z.string().min(1, {
    message: 'Formula is required',
  }),
  serialized: jsonSchema,
})
