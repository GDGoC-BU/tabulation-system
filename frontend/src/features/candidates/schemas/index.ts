import z from 'zod'
import { zStringToDate } from '@/schemas'

export const candidateGender = z.enum(['MALE', 'FEMALE', 'OTHER', 'UNKNOWN'])
export type CandidateGender = z.infer<typeof candidateGender>

export const candidateSummarySchema = z.object({
  id: z.string(),
  number: z.number(),
  firstName: z.string(),
  lastName: z.string(),
  gender: candidateGender,
  age: z.number(),
  createdAt: zStringToDate,
  updatedAt: zStringToDate,
})
export type CandidateSummary = z.infer<typeof candidateSummarySchema>

export const candidatesSchema = z.array(candidateSummarySchema)
export type Candidates = z.infer<typeof candidatesSchema>
