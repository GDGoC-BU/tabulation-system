import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/pageants/')({
  component: AdminConsolePageants,
})

function AdminConsolePageants() {
  return <div>Hello "/admin/console/pageants/"!</div>
}
