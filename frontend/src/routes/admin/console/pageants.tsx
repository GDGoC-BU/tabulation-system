import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/pageants')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/admin/console/pageants"!</div>
}
