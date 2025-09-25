import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/judges')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/admin/console/judges"!</div>
}
