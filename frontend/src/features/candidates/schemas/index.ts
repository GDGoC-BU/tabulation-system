import z from 'zod'
import { zStringToDate } from '@/schemas'
import { collegeSummarySchema } from '@/features/colleges/schemas'

export const candidateGender = z.enum(['MALE', 'FEMALE', 'OTHER'])
export type CandidateGender = z.infer<typeof candidateGender>

export const candidateHierarchySchema = z.object({
  id: z.string(),
  number: z.number(),
  firstName: z.string(),
  lastName: z.string(),
  gender: candidateGender,
  age: z.number(),
})
export type CandidateHierarchy = z.infer<typeof candidateHierarchySchema>

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

export const candidateDetailedSchema = z.object({
  id: z.string(),
  number: z.number(),
  firstName: z.string(),
  lastName: z.string(),
  gender: candidateGender,
  college: collegeSummarySchema,
  age: z.number(),
  createdAt: zStringToDate,
  updatedAt: zStringToDate,
})
export type CandidateDetailed = z.infer<typeof candidateDetailedSchema>

export const candidatesSchema = z.array(candidateSummarySchema)
export type Candidates = z.infer<typeof candidatesSchema>
