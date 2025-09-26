import { createFileRoute } from '@tanstack/react-router'
import Console from '@/components/console'
import Table from '@/components/table'
import { useCollegesQuery } from '@/features/colleges/hooks/use-colleges-query'
import { collegeTableColumns } from '@/features/colleges/components/college-table-columns'

export const Route = createFileRoute('/admin/console/colleges')({
  component: AdminColleges,
})

function AdminColleges() {
  const { data } = useCollegesQuery()

  return (
    <Console>
      <Console.Header>
        <Console.Header.Title>Colleges</Console.Header.Title>
      </Console.Header>
      <Console.Content>
        <Table.Data columns={collegeTableColumns} data={data ?? []} />
      </Console.Content>
    </Console>
  )
}
