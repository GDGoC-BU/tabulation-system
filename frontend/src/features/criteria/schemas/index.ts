import { z } from 'zod'

export const criterionSummarySchema = z.object({
  id: z.string(),
  name: z.string(),
  maxScore: z.number(),
})
export type CriterionSummary = z.infer<typeof criterionSummarySchema>

export const criterionHierarchySchema = z.object({
  id: z.string(),
  name: z.string(),
  maxScore: z.number(),
})
export type CriterionHierarchy = z.infer<typeof criterionHierarchySchema>
