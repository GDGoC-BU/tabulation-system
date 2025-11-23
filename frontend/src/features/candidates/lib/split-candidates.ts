import { candidateGender } from './../schemas/index'

export default function splitCandidates<
  T extends { gender: any; number: number },
>(candidates: Array<T>) {
  /* Group A prioritizes FEMALES */
  const groupA: Array<T> = []
  /* GroupB prioritizes MALES */
  const groupB: Array<T> = []

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
