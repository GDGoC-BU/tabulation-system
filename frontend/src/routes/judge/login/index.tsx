import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/judge/login/')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/judge/login/"!</div>
}
