import type { ColumnDef } from '@tanstack/react-table'
import type { AwardSummary } from '../schemas'
import { TextBody } from '@/components/text'

export type AwardSummaryWithRenderedFormula = Omit<AwardSummary, 'formula'> & {
  formula: React.ReactNode
}

export const awardTableColumns: Array<
  ColumnDef<AwardSummaryWithRenderedFormula>
> = [
  {
    accessorKey: 'name',
    header: 'Name',
    cell: ({ row }) => {
      const name: AwardSummaryWithRenderedFormula['name'] = row.getValue('name')
      return <TextBody>{name}</TextBody>
    },
  },
  {
    accessorKey: 'candidateLimit',
    header: 'Candidate Limit',
    cell: ({ row }) => {
      const candidateLimit: AwardSummaryWithRenderedFormula['candidateLimit'] =
        row.getValue('candidateLimit')
      return <TextBody>{candidateLimit}</TextBody>
    },
  },
  {
    accessorKey: 'formula',
    header: 'Formula',
    cell: ({ row }) => {
      const formula: AwardSummaryWithRenderedFormula['formula'] =
        row.getValue('formula')
      return (
        <TextBody className="max-w-[1000px] whitespace-normal">
          {formula}
        </TextBody>
      )
    },
  },
]
