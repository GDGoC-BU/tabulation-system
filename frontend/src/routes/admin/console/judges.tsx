import { createFileRoute } from '@tanstack/react-router'
import Console from '@/components/console'
import Table from '@/components/table'
import { judgeTableColumns } from '@/features/judges/components/pageant-table-columns'
import { useJudgesQuery } from '@/features/judges/hooks/use-judges-query'

export const Route = createFileRoute('/admin/console/judges')({
  component: AdminJudges,
})

function AdminJudges() {
  const { data } = useJudgesQuery()

  return (
    <Console>
      <Console.Header>
        <Console.Header.Title>Judges</Console.Header.Title>
      </Console.Header>
      <Console.Content>
        <Table.Data columns={judgeTableColumns} data={data ?? []} />
      </Console.Content>
    </Console>
  )
}
