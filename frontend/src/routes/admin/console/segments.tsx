import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/segments')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/admin/console/segments"!</div>
}
