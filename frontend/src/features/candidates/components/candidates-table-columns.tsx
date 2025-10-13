import type { ColumnDef } from '@tanstack/react-table'
import type { CandidateSummary } from '../schemas'
import { TextBody } from '@/components/text'

export const candidatesTableColumns: Array<ColumnDef<CandidateSummary>> = [
  {
    accessorKey: 'number',
    header: 'Number',
    cell: ({ row }) => {
      const number: CandidateSummary['number'] = row.getValue('number')
      return <TextBody>{number}</TextBody>
    },
  },
  {
    accessorKey: 'firstName',
    header: 'First Name',
    cell: ({ row }) => {
      const firstName: CandidateSummary['firstName'] = row.getValue('firstName')
      return <TextBody>{firstName}</TextBody>
    },
  },
  {
    accessorKey: 'lastName',
    header: 'Last Name',
    cell: ({ row }) => {
      const lastName: CandidateSummary['lastName'] = row.getValue('lastName')
      return <TextBody>{lastName}</TextBody>
    },
  },
  {
    accessorKey: 'gender',
    header: 'Gender',
    cell: ({ row }) => {
      const gender: CandidateSummary['gender'] = row.getValue('gender')
      return <TextBody>{gender}</TextBody>
    },
  },
  {
    accessorKey: 'age',
    header: 'Age',
    cell: ({ row }) => {
      const age: CandidateSummary['age'] = row.getValue('age')
      return <TextBody>{age}</TextBody>
    },
  },
]
