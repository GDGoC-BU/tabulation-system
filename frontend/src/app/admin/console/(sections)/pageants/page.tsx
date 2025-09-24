import Console from '@/components/console'
import Table from '@/components/table'
import { getPageants } from '@/features/pageants/actions/get-pageants'
import PageantAddFormDialog from '@/features/pageants/components/pageant-add-form-dialog'
import { pageantTableColumns } from '@/features/pageants/components/pageant-table-columns'

export default async function AdminPageants() {
  const pageants = await getPageants()

  return (
    <Console>
      <Console.Header className='flex flex-row justify-between'>
        <Console.Header.Title>Pageants</Console.Header.Title>
        <div className=''>
          <PageantAddFormDialog />
        </div>
      </Console.Header>
      <Console.Content>
        <Table.Data columns={pageantTableColumns} data={pageants} />
      </Console.Content>
    </Console>
  )
}
