import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/segments')({
  component: AdminConsoleSegments,
})

function AdminConsoleSegments() {
  return <div>Hello "/admin/console/segments"!</div>
}
