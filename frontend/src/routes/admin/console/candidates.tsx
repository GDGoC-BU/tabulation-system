import { createFileRoute } from '@tanstack/react-router'
import Console from '@/components/console'
import { useCandidatesQuery } from '@/features/candidates/hooks/use-candidates-query'
import Table from '@/components/table'
import { candidatesTableColumns } from '@/features/candidates/components/candidates-table-columns'

export const Route = createFileRoute('/admin/console/candidates')({
  component: AdminConsoleCandidates,
})

function AdminConsoleCandidates() {
  const { data } = useCandidatesQuery()

  return (
    <Console>
      <Console.Header>
        <Console.Header.Title>Candidates</Console.Header.Title>
      </Console.Header>
      <Console.Content>
        <Table.Data columns={candidatesTableColumns} data={data ?? []} />
      </Console.Content>
    </Console>
  )
}
