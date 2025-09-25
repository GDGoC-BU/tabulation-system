import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/')({
  component: AdminConsoleHome,
})

function AdminConsoleHome() {
  return <div>Hello "/admin/console/"!</div>
}
