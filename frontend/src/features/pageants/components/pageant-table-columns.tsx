'use client'

import { ColumnDef } from '@tanstack/react-table'
import { PageantStatus, PageantSummary } from '../schemas/pageant'

export const pageantTableColumns: ColumnDef<PageantSummary>[] = [
  {
    accessorKey: 'title',
    header: 'Title'
  },
  {
    accessorKey: 'status',
    header: 'Status',
    cell: ({ row }) => {
      const { value, color } = row.getValue('status') as PageantStatus

      return (
        <span
          className='mx-2 w-fit rounded-md px-4 py-2 text-center'
          style={{ backgroundColor: color }}
        >
          {value}
        </span>
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

      return formatted
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

      return formatted
    }
  }
]
