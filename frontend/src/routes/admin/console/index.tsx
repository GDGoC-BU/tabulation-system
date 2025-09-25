import { createFileRoute } from '@tanstack/react-router'
import { TextHeading } from '@/components/text'
import { useAuthentication } from '@/features/authentication/store/use-authentication'

export const Route = createFileRoute('/admin/console/')({
  component: AdminConsoleHome,
})

function AdminConsoleHome() {
  const { account } = useAuthentication((state) => state)

  return (
    <div>
      Hello "/admin/console/"!
      <TextHeading>Account: {account?.username}</TextHeading>
    </div>
  )
}
