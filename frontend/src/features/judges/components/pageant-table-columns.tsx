import type { ColumnDef } from '@tanstack/react-table'
import type { JudgeSummary } from '../schemas'
import { TextBody } from '@/components/text'

export const judgeTableColumns: Array<ColumnDef<JudgeSummary>> = [
  {
    accessorKey: 'username',
    header: 'Username',
    cell: ({ row }) => {
      const username: JudgeSummary['username'] = row.getValue('username')
      return <TextBody>{username}</TextBody>
    },
  },
  {
    accessorKey: 'isOnline',
    header: 'Online',
    cell: ({ row }) => {
      const isOnline: JudgeSummary['isOnline'] = row.getValue('isOnline')
      return <TextBody>{isOnline ? 'true' : 'false'}</TextBody>
    },
  },
]
