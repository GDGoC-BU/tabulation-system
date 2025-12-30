import { createFileRoute } from '@tanstack/react-router'
import Workspace from '@/features/blockly/workspace'

export const Route = createFileRoute('/admin/console/blockly')({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <div className="grow">
      <Workspace />
    </div>
  )
}
