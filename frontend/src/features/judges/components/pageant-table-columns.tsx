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
    accessorKey: 'honorific',
    header: 'honorific',
    cell: ({ row }) => {
      const honorific: JudgeSummary['honorific'] = row.getValue('honorific')
      return <TextBody>{honorific}</TextBody>
    },
  },
  {
    accessorKey: 'firstName',
    header: 'First Name',
    cell: ({ row }) => {
      const firstName: JudgeSummary['firstName'] = row.getValue('firstName')
      return <TextBody>{firstName}</TextBody>
    },
  },
  {
    accessorKey: 'lastName',
    header: 'Last Name',
    cell: ({ row }) => {
      const lastName: JudgeSummary['lastName'] = row.getValue('lastName')
      return <TextBody>{lastName}</TextBody>
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
