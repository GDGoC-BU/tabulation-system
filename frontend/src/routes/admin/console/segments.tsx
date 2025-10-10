import { createFileRoute } from '@tanstack/react-router'
import { useSegmentsQuery } from '@/features/segments/hooks/use-segments-query'
import Console from '@/components/console'
import { segmentTableColumns } from '@/features/segments/components/segment-table-columns'
import Table from '@/components/table'
import { TextBody } from '@/components/text'

export const Route = createFileRoute('/admin/console/segments')({
  component: AdminConsoleSegments,
})

function AdminConsoleSegments() {
  const { data, isLoading } = useSegmentsQuery()

  return (
    <Console>
      <Console.Header>
        <Console.Header.Title>Segments</Console.Header.Title>
      </Console.Header>
      <Console.Content>
        {isLoading ? (
          <TextBody>Loading...</TextBody>
        ) : (
          <Table.Data columns={segmentTableColumns} data={data ?? []} />
        )}
      </Console.Content>
    </Console>
  )
}
