import { candidateGender } from './../schemas/index'
import type { CandidateDetailed, CandidateSummary } from '../schemas'

export default function splitCandidates(
  candidates: Array<CandidateDetailed | CandidateSummary>,
) {
  /* Group A prioritizes FEMALES */
  const groupA: Array<CandidateDetailed | CandidateSummary> = []
  /* GroupB prioritizes MALES */
  const groupB: Array<CandidateDetailed | CandidateSummary> = []

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
    groupA: sortedGroupA,
    groupB: sortedGroupB,
  }
}
