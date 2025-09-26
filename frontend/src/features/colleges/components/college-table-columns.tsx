import type { ColumnDef } from '@tanstack/react-table'
import type { CollegeSummary } from '../schemas'
import { TextBody } from '@/components/text'

export const collegeTableColumns: Array<ColumnDef<CollegeSummary>> = [
  {
    accessorKey: 'code',
    header: 'Code',
    cell: ({ row }) => {
      const code: CollegeSummary['code'] = row.getValue('code')
      return <TextBody>{code}</TextBody>
    },
  },
  {
    accessorKey: 'name',
    header: 'Name',
    cell: ({ row }) => {
      const name: CollegeSummary['name'] = row.getValue('name')
      return <TextBody>{name}</TextBody>
    },
  },
]
