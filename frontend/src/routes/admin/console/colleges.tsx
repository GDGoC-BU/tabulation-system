import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/colleges')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/admin/console/colleges"!</div>
}
