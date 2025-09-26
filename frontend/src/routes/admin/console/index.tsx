import { createFileRoute } from '@tanstack/react-router'
import { TextHeading } from '@/components/text'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'

export const Route = createFileRoute('/admin/console/')({
  component: AdminConsoleHome,
})

function AdminConsoleHome() {
  const { account } = useAuthenticationStore((state) => state)

  return (
    <div>
      Hello "/admin/console/"!
      <TextHeading>Account: {account?.username}</TextHeading>
    </div>
  )
}
