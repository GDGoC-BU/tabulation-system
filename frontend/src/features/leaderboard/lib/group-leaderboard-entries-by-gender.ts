import type { LeaderboardEntrySummary } from '../schemas'
import { candidateGender } from '@/features/candidates/schemas'

export function groupLeaderboardEntriesByGender(
  entries: Array<LeaderboardEntrySummary>,
): {
  femaleCandidates: Array<LeaderboardEntrySummary>
  maleCandidates: Array<LeaderboardEntrySummary>
} {
  const femaleCandidates = entries.filter(
    (entry) => entry.candidate.gender === candidateGender.enum.FEMALE,
  )

  const maleCandidates = entries.filter(
    (entry) => entry.candidate.gender === candidateGender.enum.MALE,
  )

  return { femaleCandidates, maleCandidates }
}
