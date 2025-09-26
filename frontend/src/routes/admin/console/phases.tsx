import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/phases')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/admin/console/phases"!</div>
}
