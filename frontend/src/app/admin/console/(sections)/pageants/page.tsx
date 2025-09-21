import Console from '@/components/console'
import Table from '@/components/table'
import { getPageants } from '@/features/pageants/actions/get-pageants'
import { pageantTableColumns } from '@/features/pageants/components/pageant-table-columns'

export default async function AdminPageants() {
  const pageants = await getPageants()

  return (
    <Console>
      <Console.Header>
        <Console.Header.Title>Pageants</Console.Header.Title>
      </Console.Header>
      <Console.Content>
        <Table.Data columns={pageantTableColumns} data={pageants} />
      </Console.Content>
    </Console>
  )
}
