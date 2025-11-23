import BreakdownRenderer from '../../formula/components/breakdown-renderer'
import type { ColumnDef } from '@tanstack/react-table'
import type { CandidateSegmentQualificationSummary } from '../schemas'
import { TextBody } from '@/components/text'
import capitalizeWords from '@/lib/capitalize-words'
import { cn } from '@/lib/utils'

export const segmentCandidateQualifications: Array<
  ColumnDef<CandidateSegmentQualificationSummary>
> = [
  {
    id: 'rank',
    accessorKey: 'rank',
    header: 'Rank',
    cell: ({ row }) => {
      const rank: CandidateSegmentQualificationSummary['rank'] =
        row.original.rank
      const isTied: CandidateSegmentQualificationSummary['isTied'] =
        row.original.isTied

      if (!rank) {
        return (
          <div className="w-full grid place-items-center">
            <TextBody>No rank available</TextBody>
          </div>
        )
      }

      return (
        <div
          className={cn(
            'w-full flex flex-row items-center justify-center',
            isTied
              ? 'border border-destructive px-2 py-1 rounded-full [&>*]:text-destructive'
              : 'border-none px-0 py-0 rounded-none',
          )}
        >
          <TextBody>{rank}</TextBody>
          {isTied && (
            <div className="self-stretch w-[1px] mx-1 bg-destructive" />
          )}
          {isTied && <TextBody>Tie</TextBody>}
        </div>
      )
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
    id: 'candidateCollege',
    header: 'College',
    cell: ({ row }) => {
      const candidate: CandidateSegmentQualificationSummary['candidate'] =
        row.original.candidate
      return <TextBody>{candidate.college.code.toUpperCase()}</TextBody>
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
