import z from 'zod'

export const zStringToDate = z.string().transform((val, ctx) => {
  const date = new Date(val)
  if (isNaN(date.getTime())) {
    ctx.addIssue({
      code: 'custom',
      message: `Invalid date: ${val}`
    })
    return z.NEVER
  }
  return date
})
