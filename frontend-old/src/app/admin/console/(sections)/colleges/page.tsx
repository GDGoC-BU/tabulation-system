import Console from '@/components/console'
import Table from '@/components/table'
import { getColleges } from '@/features/colleges/actions/get-colleges'
import { collegeTableColumns } from '@/features/colleges/components/college-table-columns'

export default async function AdminColleges() {
  const colleges = await getColleges()

  return (
    <Console>
      <Console.Header>
        <Console.Header.Title>Colleges</Console.Header.Title>
      </Console.Header>
      <Console.Content>
        <Table.Data data={colleges} columns={collegeTableColumns} />
      </Console.Content>
    </Console>
  )
}
