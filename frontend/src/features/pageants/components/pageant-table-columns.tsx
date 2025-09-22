'use client'

import { ColumnDef } from '@tanstack/react-table'
import { PageantStatus, PageantSummary } from '../schemas/pageant'
import { TextBody } from '@/components/text'
import PageantEditFormModal from './pageant-edit-form-modal'

export const pageantTableColumns: ColumnDef<PageantSummary>[] = [
  {
    accessorKey: 'title',
    header: 'Title',
    cell: ({ row }) => {
      const title = row.getValue('title') as string
      return <TextBody>{title}</TextBody>
    }
  },
  {
    accessorKey: 'status',
    header: 'Status',
    cell: ({ row }) => {
      const { value, color } = row.getValue('status') as PageantStatus

      return (
        <div
          className='mx-2 w-fit rounded-md px-4 py-2 text-center'
          style={{ backgroundColor: color }}
        >
          <TextBody>{value}</TextBody>
        </div>
      )
    }
  },
  {
    accessorKey: 'startedAt',
    header: 'Started At',
    cell: ({ row }) => {
      const date = row.getValue('startedAt') as Date

      if (!date) {
        return null
      }

      const formatted = new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: 'long',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: true
      }).format(date)

      return <TextBody>{formatted}</TextBody>
    }
  },
  {
    accessorKey: 'endedAt',
    header: 'Ended At',
    cell: ({ row }) => {
      const date = row.getValue('endedAt') as Date

      if (!date) {
        return null
      }

      const formatted = new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: 'long',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: true
      }).format(date)

      return <TextBody>{formatted}</TextBody>
    }
  },
  {
    id: 'actions',
    cell: ({ row }) => {
      const pageant = row.original

      return <PageantEditFormModal pageant={pageant} />
    }
  }
]
