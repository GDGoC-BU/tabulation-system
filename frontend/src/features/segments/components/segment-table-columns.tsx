import { Link } from '@tanstack/react-router'
import { Ellipsis } from 'lucide-react'
import type { ColumnDef } from '@tanstack/react-table'
import type { SegmentSummary } from '../schemas'
import { TextBody } from '@/components/text'

export type SegmentSummaryWithRenderedFormula = Omit<
  SegmentSummary,
  'formula'
> & {
  formula: React.ReactNode
}

export const segmentTableColumns: Array<
  ColumnDef<SegmentSummaryWithRenderedFormula>
> = [
  {
    accessorKey: 'name',
    header: 'Name',
    cell: ({ row }) => {
      const name: SegmentSummaryWithRenderedFormula['name'] =
        row.getValue('name')
      return <TextBody>{name}</TextBody>
    },
  },
  {
    accessorKey: 'phase',
    header: 'Phase',
    cell: ({ row }) => {
      const phase: SegmentSummaryWithRenderedFormula['phase'] =
        row.getValue('phase')

      return <TextBody>{phase.name}</TextBody>
    },
  },
  {
    accessorKey: 'sequence',
    header: 'Sequence',
    cell: ({ row }) => {
      const sequence: SegmentSummaryWithRenderedFormula['sequence'] =
        row.getValue('sequence')

      return <TextBody>{sequence}</TextBody>
    },
  },

  {
    accessorKey: 'candidateLimit',
    header: 'Candidate Limit',
    cell: ({ row }) => {
      const candidateLimit: SegmentSummaryWithRenderedFormula['candidateLimit'] =
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
      const formula: SegmentSummaryWithRenderedFormula['formula'] =
        row.getValue('formula')

      if (!formula) {
        return <TextBody>None</TextBody>
      }

      return <div className="max-w-[1000px] whitespace-normal">{formula}</div>
    },
  },
  {
    id: 'actions',
    cell: ({ row }) => {
      const segment = row.original
      return (
        <Link
          to={'/admin/console/segments/$segmentId/edit'}
          params={{ segmentId: segment.id }}
        >
          <Ellipsis />
        </Link>
      )
    },
  },
]
