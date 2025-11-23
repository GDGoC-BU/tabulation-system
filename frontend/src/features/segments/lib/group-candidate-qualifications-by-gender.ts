import type { CandidateQualifications } from '@/features/segments/schemas'
import { candidateGender } from '@/features/candidates/schemas'

export function groupCandidateQualificationsByGender(
  candidateQualifications: CandidateQualifications | undefined,
): {
  femaleCandidateQualifications: CandidateQualifications
  maleCandidateQualifications: CandidateQualifications
} {
  if (!candidateQualifications)
    return {
      femaleCandidateQualifications: [],
      maleCandidateQualifications: [],
    }

  const femaleGroup = candidateQualifications
    .filter((entry) => entry.candidate.gender === candidateGender.enum.FEMALE)
    .sort((a, b) => {
      if (a.rank && b.rank) {
        return a.rank - b.rank
      }
      return a.candidate.number - b.candidate.number
    })

  const maleGroup = candidateQualifications
    .filter((entry) => entry.candidate.gender === candidateGender.enum.MALE)
    .sort((a, b) => {
      if (a.rank && b.rank) {
        return a.rank - b.rank
      }
      return a.candidate.number - b.candidate.number
    })

  return {
    femaleCandidateQualifications: femaleGroup,
    maleCandidateQualifications: maleGroup,
  }
}
