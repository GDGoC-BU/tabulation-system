import BreakdownRenderer from '../../formula/components/breakdown-renderer'
import type { ColumnDef } from '@tanstack/react-table'
import type { CandidateSegmentQualificationSummary } from '../schemas'
import { TextBody } from '@/components/text'
import capitalizeWords from '@/lib/capitalize-words'

export const segmentCandidateQualifications: Array<
  ColumnDef<CandidateSegmentQualificationSummary>
> = [
  {
    id: 'rank',
    header: 'Rank',
    cell: ({ row }) => {
      return <TextBody>{row.index + 1}</TextBody>
    },
  },
  {
    id: 'candidateNumber',
    header: 'Candidate Number',
    cell: ({ row }) => {
      const candidate: CandidateSegmentQualificationSummary['candidate'] =
        row.original.candidate
      return <TextBody>#{candidate.number}</TextBody>
    },
  },
  {
    id: 'candidateName',
    header: 'Candidate Name',
    cell: ({ row }) => {
      const candidate: CandidateSegmentQualificationSummary['candidate'] =
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
    accessorKey: 'score',
    header: 'Score',
    cell: ({ row }) => {
      const score: CandidateSegmentQualificationSummary['score'] =
        row.getValue('score')
      const formattedScore =
        score === 0 || score === null ? 0 : score.toFixed(3)
      return <TextBody>{formattedScore}</TextBody>
    },
  },
  {
    accessorKey: 'criteriaBreakdown',
    header: 'Breakdown',
    cell: ({ row, table }) => {
      const leaderboard: CandidateSegmentQualificationSummary = row.original
      const formula: string = table.options.meta?.formula ?? null

      if (!formula || formula.length === 0) {
        return <TextBody>No breakdown available</TextBody>
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
