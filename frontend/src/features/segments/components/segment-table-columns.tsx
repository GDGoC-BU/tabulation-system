import type { ColumnDef } from '@tanstack/react-table'
import type { SegmentSummary } from '../schemas'
import { TextBody } from '@/components/text'

export const segmentTableColumns: Array<ColumnDef<SegmentSummary>> = [
  {
    accessorKey: 'name',
    header: 'Name',
    cell: ({ row }) => {
      const name: SegmentSummary['name'] = row.getValue('name')
      return <TextBody>{name}</TextBody>
    },
  },
  {
    accessorKey: 'phase',
    header: 'Phase',
    cell: ({ row }) => {
      const phase: SegmentSummary['phase'] = row.getValue('phase')

      return <TextBody>{phase.name}</TextBody>
    },
  },
  {
    accessorKey: 'sequence',
    header: 'Sequence',
    cell: ({ row }) => {
      const sequence: SegmentSummary['sequence'] = row.getValue('sequence')

      return <TextBody>{sequence}</TextBody>
    },
  },

  {
    accessorKey: 'candidateLimit',
    header: 'Candidate Limit',
    cell: ({ row }) => {
      const candidateLimit: SegmentSummary['candidateLimit'] =
        row.getValue('candidateLimit')
      const label = candidateLimit ? candidateLimit : 'None'

      return (
        <div className="mx-2 w-fit rounded-md px-4 py-2 text-center">
          <TextBody>{label}</TextBody>
        </div>
      )
    },
  },
  {
    accessorKey: 'formula',
    header: 'Formula',
    cell: ({ row }) => {
      const formula: SegmentSummary['formula'] = row.getValue('formula')

      /* Must be rendered properly! */
      return (
        <div className="mx-2 w-fit rounded-md px-4 py-2 text-center">
          <TextBody>{formula}</TextBody>
        </div>
      )
    },
  },
]
