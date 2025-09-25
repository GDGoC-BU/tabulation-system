import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/colleges/')({
  component: AdminConsoleColleges,
})

function AdminConsoleColleges() {
  return <div>Hello "/admin/console/colleges/"!</div>
}
