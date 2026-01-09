import z from 'zod'
import { criteriaBreakdownSchema, phaseSegmentStatusValue } from '@/schemas'
import { phaseSummarySchema } from '@/features/phases/schemas'
import {
  criterionHierarchySchema,
  criterionSummarySchema,
} from '@/features/criteria/schemas'
import {
  candidateHierarchySchema,
  candidateSummarySchema,
} from '@/features/candidates/schemas'
import { formulaSchema } from '@/features/formula/schemas'

export const candidateSegmentQualificationHierarchySchema = z.object({
  id: z.string(),
  candidate: candidateHierarchySchema,
  isQualified: z.boolean(),
  score: z.union([z.null(), z.number()]),
})
export type CandidateSegmentQualificationHierarchy = z.infer<
  typeof candidateSegmentQualificationHierarchySchema
>

export const candidateSegmentQualificationSummarySchema = z.object({
  id: z.string(),
  candidate: candidateSummarySchema,
  rank: z.union([z.null(), z.number()]),
  isQualified: z.boolean(),
  score: z.union([z.null(), z.number()]),
  isTied: z.boolean(),
  criteriaBreakdown: z.union([z.null(), z.array(criteriaBreakdownSchema)]),
})
export type CandidateSegmentQualificationSummary = z.infer<
  typeof candidateSegmentQualificationSummarySchema
>

export const candidateQualificationsSchema = z.array(
  candidateSegmentQualificationSummarySchema,
)
export type CandidateQualifications = z.infer<
  typeof candidateQualificationsSchema
>

export const segmentSummarySchema = z.object({
  id: z.string(),
  name: z.string(),
  sequence: z.number(),
  candidateLimit: z.union([z.null(), z.number()]),
  formula: z.union([z.null(), formulaSchema]),
  status: phaseSegmentStatusValue,
  phase: z.lazy(() => {
    /* Circular import patch! segments/schema imports from phases/schema.
       Code below doesn't load the schema properly. This somehow solves the issue */
    /* const { phaseSummarySchema } = require('@/features/phases/schemas') */
    return phaseSummarySchema
  }),
})
export type SegmentSummary = z.infer<typeof segmentSummarySchema>

export const segmentDetailedSchema = z.object({
  id: z.string(),
  name: z.string(),
  sequence: z.number(),
  candidateLimit: z.union([z.null(), z.number()]),
  formula: z.union([z.null(), formulaSchema]),
  status: phaseSegmentStatusValue,
  phase: z.lazy(() => {
    return phaseSummarySchema
  }),
  criteria: z.array(criterionSummarySchema),
  candidateQualifications: candidateQualificationsSchema,
})
export type SegmentDetailed = z.infer<typeof segmentDetailedSchema>

export const segmentHierarchySchema = z.object({
  id: z.string(),
  name: z.string(),
  sequence: z.number(),
  candidateLimit: z.union([z.null(), z.number()]),
  formula: z.union([z.null(), formulaSchema]),
  status: phaseSegmentStatusValue,
  criteria: z.array(criterionHierarchySchema),
  candidateQualifications: z.array(
    candidateSegmentQualificationHierarchySchema,
  ),
})
export type SegmentHierarchy = z.infer<typeof segmentHierarchySchema>

export const segmentsSchema = z.array(segmentSummarySchema)
export type Segments = z.infer<typeof segmentsSchema>

export const segmentEditFormSchema = z.object({
  id: z.string(),
  name: z.string(),
  candidateLimit: z.union([
    z.null(),
    z.coerce.number({
      message: 'Invalid value! Only positive numbers are allowed',
    }),
  ]),
  formula: z.union([z.null(), formulaSchema]),
})
export type SegmentEditForm = z.infer<typeof segmentEditFormSchema>
