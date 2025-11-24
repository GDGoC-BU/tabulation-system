import type { AwardLeaderboards } from '@/features/awards/schemas'
import { candidateGender } from '@/features/candidates/schemas'

export function groupLeaderboardByGender(
  leaderboard: AwardLeaderboards | undefined,
): {
  groupA: AwardLeaderboards
  groupB: AwardLeaderboards
} {
  if (!leaderboard)
    return {
      groupA: [],
      groupB: [],
    }

  const groupA = leaderboard.filter(
    (entry) => entry.candidate.gender === candidateGender.enum.FEMALE,
  )

  const groupB = leaderboard.filter(
    (entry) => entry.candidate.gender === candidateGender.enum.MALE,
  )

  return { groupA, groupB }
}
