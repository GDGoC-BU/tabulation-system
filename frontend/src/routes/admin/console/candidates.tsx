import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console/candidates')({
  component: AdminConsoleCandidates,
})

function AdminConsoleCandidates() {
  return <div>Hello "/admin/console/candidates"!</div>
}
