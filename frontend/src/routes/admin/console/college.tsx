import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/college')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/admin/console/college"!</div>
}
