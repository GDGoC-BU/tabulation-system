import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/dashboard/')({
  component: AdminConsoleDashboard,
})

function AdminConsoleDashboard() {
  return <div>Hello "/admin/console/dashboard/"!</div>
}
