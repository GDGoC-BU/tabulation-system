import { createFileRoute } from '@tanstack/react-router'
import Console from '@/components/console'
import Table from '@/components/table'
import { awardTableColumns } from '@/features/awards/components/college-table-columns'
import { useAwardsQuery } from '@/features/awards/hooks/use-awards-query'

export const Route = createFileRoute('/admin/console/awards')({
  component: AdminConsoleAwards,
})

function AdminConsoleAwards() {
  const { data } = useAwardsQuery()

  return (
    <Console>
      <Console.Header className="flex flex-row justify-between">
        <Console.Header.Title>Awards</Console.Header.Title>
        <div className=""></div>
      </Console.Header>
      <Console.Content>
        <Table.Data columns={awardTableColumns} data={data ?? []} />
      </Console.Content>
    </Console>
  )
}
