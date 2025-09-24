'use client'

import { ColumnDef } from '@tanstack/react-table'
import { CollegeSummary } from '../schemas'
import { TextBody } from '@/components/text'

export const collegeTableColumns: ColumnDef<CollegeSummary>[] = [
  {
    accessorKey: 'code',
    header: 'Code',
    cell: ({ row }) => {
      const code = row.getValue('code') as string
      return <TextBody>{code}</TextBody>
    }
  },
  {
    accessorKey: 'name',
    header: 'Name',
    cell: ({ row }) => {
      const name = row.getValue('name') as string
      return <TextBody>{name}</TextBody>
    }
  }
]
