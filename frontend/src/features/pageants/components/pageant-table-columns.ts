'use client'

import { ColumnDef } from '@tanstack/react-table'
import { PageantSummary } from '../schemas/pageant'

export const pageantTableColumns: ColumnDef<PageantSummary>[] = [
  {
    accessorKey: 'title',
    header: 'Title'
  },
  {
    accessorKey: 'status',
    header: 'Status'
  },
  {
    accessorKey: 'startedAt',
    header: 'Started At'
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
        day: '2-digit'
      }).format(date)

      return formatted
    }
  },
  {
    accessorKey: 'createdAt',
    header: 'Created At',
    cell: ({ row }) => {
      const date = row.getValue('createdAt') as Date
      const formatted = new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: 'long',
        day: '2-digit'
      }).format(date)

      return formatted
    }
  }
]
