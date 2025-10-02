import { createFileRoute } from '@tanstack/react-router'
import Console from '@/components/console'

export const Route = createFileRoute('/admin/console/awards')({
  component: AdminConsoleAwards,
})

function AdminConsoleAwards() {
  return (
    <Console>
      <Console.Header>
        <Console.Header.Title>Awards</Console.Header.Title>
      </Console.Header>
      <Console.Content>Awards</Console.Content>
    </Console>
  )
}
