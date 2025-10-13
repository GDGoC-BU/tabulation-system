import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/phases')({
  component: AdminConsolePhases,
})

function AdminConsolePhases() {
  return <div>Hello "/admin/console/phases"!</div>
}
