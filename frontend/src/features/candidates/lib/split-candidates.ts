import type { CandidateDetailed } from '../schemas'

/* Split an array of candidates into 2 groups by gender: group A and group b.
   Group A prioritizes females and Group B prioritizes males. This function
   determines which group a candidate of gender OTHER gets placed.
   
   Allowed combinations:
   [FEMALE , MALE]
   [OTHER , MALE]
   [FEMALE, OTHER]
   [OTHER, OTHER]
   
   [MALE , MALE] and [FEMALE , FEMALE] are
   technicallynot allowed but are pushed anyway */
export default function splitCandidates(candidates: Array<CandidateDetailed>) {
  const collegeMap = new Map<string, Array<CandidateDetailed>>()

  /* Group candidates per college */
  for (const candidate of candidates) {
    const collegeId = candidate.college.id
    if (!collegeMap.has(collegeId)) {
      collegeMap.set(collegeId, [])
    }
    collegeMap.get(collegeId)!.push(candidate)
  }

  /* Group A prioritizes FEMALES */
  const groupA: Array<CandidateDetailed> = []
  /* GroupB prioritizes MALES */
  const groupB: Array<CandidateDetailed> = []

  /* Go through the college candidate pairs and
     determine which group they belong to */
  for (const pair of collegeMap.values()) {
    if (pair.length !== 2) {
      console.warn('College does not have exactly 2 candidates:', pair)
      continue
    }

    const [candidate1, candidate2] = pair
    const genders = [candidate1.gender, candidate2.gender]

    if (genders.includes('FEMALE') && genders.includes('MALE')) {
      /* FEMALE + MALE */
      const female = candidate1.gender === 'FEMALE' ? candidate1 : candidate2
      const male = candidate1.gender === 'MALE' ? candidate1 : candidate2
      groupA.push(female)
      groupB.push(male)
    } else if (genders.includes('FEMALE') && genders.includes('OTHER')) {
      /* FEMALE + OTHER */
      const female = candidate1.gender === 'FEMALE' ? candidate1 : candidate2
      const other = candidate1.gender === 'OTHER' ? candidate1 : candidate2
      groupA.push(female)
      groupB.push(other)
    } else if (genders.includes('MALE') && genders.includes('OTHER')) {
      /* MALE + OTHER */
      const male = candidate1.gender === 'MALE' ? candidate1 : candidate2
      const other = candidate1.gender === 'OTHER' ? candidate1 : candidate2
      groupA.push(other)
      groupB.push(male)
    } else if (genders[0] === 'OTHER' && genders[1] === 'OTHER') {
      /* OTHER + OTHER */
      groupA.push(candidate1)
      groupB.push(candidate2)
    } else {
      console.warn(
        'Candidate pairs have a not allowed gender combination! [FEMALE,FEMALE] or []MALE,MALE',
      )
      groupA.push(candidate1)
      groupB.push(candidate2)
    }
  }

  /* Final sort by candidate number. This guarantees that
     the group elements are ordered by college */
  const sortedGroupA = groupA.sort((a, b) => a.number - b.number)
  const sortedGroupB = groupB.sort((a, b) => a.number - b.number)

  return {
    groupA: sortedGroupA,
    groupB: sortedGroupB,
  }
}
