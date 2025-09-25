import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/dashboard')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/admin/console/dashboard"!</div>
}
