import { createFileRoute } from '@tanstack/react-router'
import { useQuery } from '@tanstack/react-query'
import Console from '@/components/console'
import Table from '@/components/table'
import { pageantTableColumns } from '@/features/pageants/components/pageant-table-columns'
import PageantAddFormDialog from '@/features/pageants/components/pageant-add-form-dialog'
import pageantsQueryOptions from '@/features/pageants/query-options/pageants-query-options'

export const Route = createFileRoute('/admin/console/pageants/')({
  component: AdminConsolePageants,
})

function AdminConsolePageants() {
  const { data } = useQuery(pageantsQueryOptions())

  return (
    <Console>
      <Console.Header className="flex flex-row justify-between">
        <Console.Header.Title>Pageants</Console.Header.Title>
        <div>
          <PageantAddFormDialog />
        </div>
      </Console.Header>
      <Console.Content>
        <Table.Data columns={pageantTableColumns} data={data ?? []} />
      </Console.Content>
    </Console>
  )
}
