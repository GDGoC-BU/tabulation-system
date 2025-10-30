import { candidateGender } from './../schemas/index'
import type {
  CandidateDetailed,
  CandidateHierarchy,
  CandidateSummary,
} from '../schemas'

export default function splitCandidates(
  candidates: Array<CandidateDetailed | CandidateSummary | CandidateHierarchy>,
) {
  /* Group A prioritizes FEMALES */
  const groupA: Array<
    CandidateDetailed | CandidateSummary | CandidateHierarchy
  > = []
  /* GroupB prioritizes MALES */
  const groupB: Array<
    CandidateDetailed | CandidateSummary | CandidateHierarchy
  > = []

  const FEMALE = candidateGender.enum.FEMALE
  const MALE = candidateGender.enum.MALE

  for (const candidate of candidates) {
    if (candidate.gender === FEMALE) {
      groupA.push(candidate)
    } else if (candidate.gender === MALE) {
      groupB.push(candidate)
    }
  }

  const sortedGroupA = groupA.sort((a, b) => a.number - b.number)
  const sortedGroupB = groupB.sort((a, b) => a.number - b.number)

  return {
    femaleCandidates: sortedGroupA,
    maleCandidates: sortedGroupB,
  }
}
