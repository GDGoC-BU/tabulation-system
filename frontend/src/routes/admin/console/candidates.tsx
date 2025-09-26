import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/candidates')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/admin/console/candidates"!</div>
}
