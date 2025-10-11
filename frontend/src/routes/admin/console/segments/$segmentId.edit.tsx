import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/segments/$segmentId/edit')(
  {
    component: RouteComponent,
  },
)

function RouteComponent() {
  return <div>Hello "/admin/console/segments/$segmentId/edit"!</div>
}
