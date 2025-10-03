import { Link, createFileRoute } from '@tanstack/react-router'
import Console from '@/components/console'
import Table from '@/components/table'
import { useAwardsQuery } from '@/features/awards/hooks/use-awards-query'
import { Button } from '@/components/ui/button'
import { awardTableColumns } from '@/features/awards/components/award-table-columns'

export const Route = createFileRoute('/admin/console/awards/')({
  component: AdminConsoleAwards,
})

function AdminConsoleAwards() {
  const { data } = useAwardsQuery()

  return (
    <Console>
      <Console.Header className="flex flex-row justify-between items-center">
        <Console.Header.Title>Awards</Console.Header.Title>
        <div className="">
          <Button asChild>
            <Link to="/admin/console/awards/add">Add Award</Link>
          </Button>
        </div>
      </Console.Header>
      <Console.Content>
        <Table.Data columns={awardTableColumns} data={data ?? []} />
      </Console.Content>
    </Console>
  )
}
