import type { CandidateQualifications } from '@/features/segments/schemas'
import { candidateGender } from '@/features/candidates/schemas'

export function groupCandidateQualificationsByGender(
  candidateQualifications: CandidateQualifications | undefined,
): {
  groupA: CandidateQualifications
  groupB: CandidateQualifications
} {
  if (!candidateQualifications)
    return {
      groupA: [],
      groupB: [],
    }

  const groupA = candidateQualifications.filter(
    (entry) => entry.candidate.gender === candidateGender.enum.FEMALE,
  )

  const groupB = candidateQualifications.filter(
    (entry) => entry.candidate.gender === candidateGender.enum.MALE,
  )

  return { groupA, groupB }
}
