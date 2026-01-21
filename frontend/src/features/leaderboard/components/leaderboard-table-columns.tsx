import BreakdownRenderer from '../../formula/deprecated-components/breakdown-renderer'
import type { ColumnDef } from '@tanstack/react-table'
import type { LeaderboardEntrySummary } from '../schemas'
import { TextBody } from '@/components/text'
import capitalizeWords from '@/lib/capitalize-words'

export const leadboardTableColumns: Array<ColumnDef<LeaderboardEntrySummary>> =
  [
    {
      id: 'rank',
      header: 'Rank',
      cell: ({ row }) => {
        const rank: LeaderboardEntrySummary['rank'] = row.getValue('rank')
        return <TextBody>{rank ? rank : '-'}</TextBody>
      },
    },
    {
      id: 'candidateNumber',
      header: 'Candidate Number',
      cell: ({ row }) => {
        const candidate: LeaderboardEntrySummary['candidate'] =
          row.original.candidate
        return <TextBody>#{candidate.number}</TextBody>
      },
    },
    {
      id: 'candidateName',
      header: 'Candidate Name',
      cell: ({ row }) => {
        const candidate: LeaderboardEntrySummary['candidate'] =
          row.original.candidate
        return (
          <TextBody>
            {capitalizeWords(candidate.firstName)}{' '}
            {capitalizeWords(candidate.lastName)}
          </TextBody>
        )
      },
    },
    {
      id: 'candidateCollege',
      header: 'Candidate College',
      cell: ({ row }) => {
        const candidate: LeaderboardEntrySummary['candidate'] =
          row.original.candidate
        return <TextBody>{candidate.college.code}</TextBody>
      },
    },
    {
      accessorKey: 'score',
      header: 'Score',
      cell: ({ row }) => {
        const score: LeaderboardEntrySummary['score'] = row.getValue('score')
        const formattedScore = score === 0 ? score : score
        return <TextBody>{formattedScore}</TextBody>
      },
    },
    {
      accessorKey: 'criteriaBreakdown',
      header: 'Breakdown',
      cell: ({ row, table }) => {
        const leaderboard: LeaderboardEntrySummary = row.original
        const formula = table.options.meta?.formula

        if (!formula || formula.text.length === 0) {
          return <TextBody>No formula passed</TextBody>
        }

        return (
          <BreakdownRenderer
            formula={formula.text}
            breakdown={leaderboard.criteriaBreakdown}
          />
        )
      },
    },
  ]
