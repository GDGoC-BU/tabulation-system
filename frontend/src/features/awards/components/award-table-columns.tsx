import type { ColumnDef } from '@tanstack/react-table'
import type { AwardSummary } from '../schemas'
import { TextBody } from '@/components/text'

export const awardTableColumns: Array<ColumnDef<AwardSummary>> = [
  {
    accessorKey: 'name',
    header: 'Name',
    cell: ({ row }) => {
      const name: AwardSummary['name'] = row.getValue('name')
      return <TextBody>{name}</TextBody>
    },
  },
  {
    accessorKey: 'candidateLimit',
    header: 'Candidate Limit',
    cell: ({ row }) => {
      const candidateLimit: AwardSummary['candidateLimit'] =
        row.getValue('candidateLimit')
      return <TextBody>{candidateLimit}</TextBody>
    },
  },
  {
    accessorKey: 'formula',
    header: 'Formula',
    cell: ({ row }) => {
      const formula: AwardSummary['formula'] = row.getValue('formula')
      return (
        <TextBody className="max-w-[1000px] whitespace-normal">
          {formula}
        </TextBody>
      )
    },
  },
]
