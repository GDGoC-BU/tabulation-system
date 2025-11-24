import PageantTitle from './pageant-title'
import PageantEditFormDialog from './pageant-edit-form-dialog'
import type { ColumnDef } from '@tanstack/react-table'
import type { PageantSummary } from '../schemas'
import { TextBody } from '@/components/text'

export const pageantTableColumns: Array<ColumnDef<PageantSummary>> = [
  {
    accessorKey: 'title',
    header: 'Title',
    cell: ({ row }) => {
      const pageant = row.original
      return <PageantTitle pageant={pageant}>{pageant.title}</PageantTitle>
    },
  },
  {
    accessorKey: 'status',
    header: 'Status',
    cell: ({ row }) => {
      const status: PageantSummary['status'] = row.getValue('status')

      return (
        <div
          className="mx-2 w-fit rounded-md px-4 py-2 text-center"
          style={{ backgroundColor: status.color }}
        >
          <TextBody>{status.value}</TextBody>
        </div>
      )
    },
  },
  {
    accessorKey: 'startedAt',
    header: 'Started At',
    cell: ({ row }) => {
      const date: PageantSummary['startedAt'] = row.getValue('startedAt')

      if (!date) {
        return <TextBody>-</TextBody>
      }

      const formatted = new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: 'long',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: true,
      }).format(date)

      return <TextBody>{formatted}</TextBody>
    },
  },
  {
    accessorKey: 'endedAt',
    header: 'Ended At',
    cell: ({ row }) => {
      const date: PageantSummary['endedAt'] = row.getValue('endedAt')

      if (!date) {
        return <TextBody>-</TextBody>
      }

      const formatted = new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: 'long',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: true,
      }).format(date)

      return <TextBody>{formatted}</TextBody>
    },
  },
  // {
  //   id: 'soft-reset',
  //   cell: ({ row }) => {
  //     const pageant = row.original
  //     return (
  //       <Link
  //         to={'/admin/console/pageants/$pageantId/soft-reset'}
  //         params={{ pageantId: pageant.id }}
  //       >
  //         Soft Reset
  //       </Link>
  //     )
  //   },
  // },
  {
    id: 'actions',
    cell: ({ row }) => {
      const pageant = row.original

      return <PageantEditFormDialog pageant={pageant} />
    },
  },
]
