import { createFileRoute } from '@tanstack/react-router'
import { useQuery } from '@tanstack/react-query'
import Console from '@/components/console'
import Table from '@/components/table'
import { pageantTableColumns } from '@/features/pageants/components/pageant-table-columns'
import { usePageantsQuery } from '@/features/pageants/hooks/use-pageants-query'
import api from '@/lib/axios'
import { pageantsSchema } from '@/features/pageants/schemas'
import errorResolver from '@/lib/error-resolver'

export const Route = createFileRoute('/admin/console/pageants')({
  component: AdminConsolePageants,
})

function AdminConsolePageants() {
  const { data } = usePageantsQuery()

  return (
    <Console>
      <Console.Header className="flex flex-row justify-between">
        <Console.Header.Title>Pageants</Console.Header.Title>
        <div className="">{/* <PageantAddFormDialog /> */}</div>
      </Console.Header>
      <Console.Content>
        <Table.Data columns={pageantTableColumns} data={data ?? []} />
      </Console.Content>
    </Console>
  )
}
