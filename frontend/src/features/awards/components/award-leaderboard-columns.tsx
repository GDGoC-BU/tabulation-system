import BreakdownRenderer from '../../formula/components/breakdown-renderer'
import type { ColumnDef } from '@tanstack/react-table'
import type { AwardLeaderboardSummary } from '../schemas'
import { TextBody } from '@/components/text'
import capitalizeWords from '@/lib/capitalize-words'

export const awardLeadboardTableColumns: Array<
  ColumnDef<AwardLeaderboardSummary>
> = [
  {
    accessorKey: '',
    header: 'Rank',
    cell: ({ cell, row }) => {
      return <TextBody>{row.index + 1}</TextBody>
    },
  },
  {
    accessorKey: 'candidate',
    header: 'Candidate Number',
    cell: ({ row }) => {
      const candidate: AwardLeaderboardSummary['candidate'] =
        row.getValue('candidate')
      return <TextBody>#{candidate.number}</TextBody>
    },
  },
  {
    accessorKey: 'candidate',
    header: 'Candidate Name',
    cell: ({ row }) => {
      const candidate: AwardLeaderboardSummary['candidate'] =
        row.getValue('candidate')
      return (
        <TextBody>
          {capitalizeWords(candidate.firstName)}{' '}
          {capitalizeWords(candidate.lastName)}
        </TextBody>
      )
    },
  },
  {
    accessorKey: 'score',
    header: 'Score',
    cell: ({ row }) => {
      const score: AwardLeaderboardSummary['score'] = row.getValue('score')
      const formattedScore = score === 0 ? score : score.toFixed(3)
      return <TextBody>{formattedScore}</TextBody>
    },
  },
  {
    accessorKey: 'criteriaBreakdown',
    header: 'Breakdown',
    cell: ({ row, table }) => {
      const leaderboard: AwardLeaderboardSummary = row.original
      const formula: string = table.options.meta.formula ?? ''

      if (!formula || formula.length === 0) {
        return <TextBody>No formula passed</TextBody>
      }

      return (
        <BreakdownRenderer
          formula={formula}
          breakdown={leaderboard.criteriaBreakdown}
        />
      )
    },
  },
]
