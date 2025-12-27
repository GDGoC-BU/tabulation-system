import { createFileRoute } from '@tanstack/react-router'
import BlocklyWorkspace from '@/features/blockly/components/blockly-workspace'

export const Route = createFileRoute('/admin/console/blockly')({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <div className="grow">
      <BlocklyWorkspace />
    </div>
  )
}
