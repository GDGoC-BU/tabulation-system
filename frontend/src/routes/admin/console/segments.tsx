import { createFileRoute } from '@tanstack/react-router'
import { useSegmentsQuery } from '@/features/segments/hooks/use-segments-query'

export const Route = createFileRoute('/admin/console/segments')({
  component: AdminConsoleSegments,
})

function AdminConsoleSegments() {
  const { data } = useSegmentsQuery()

  return <div>Hello "/admin/console/segments"!</div>
}
