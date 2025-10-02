import { useNavigate } from '@tanstack/react-router'
import { LogOut } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'
import { useSelectedPageantIdStore } from '@/features/pageants/store/use-selected-pageant-id-store'

export default function Horizontal() {
  const { logout } = useAuthenticationStore((state) => state)
  const { setSelectedPageantId: setPageantId } = useSelectedPageantIdStore(
    (state) => state,
  )
  const navigation = useNavigate()

  function logoutAccount() {
    navigation({ to: '/' })
    /* Add short delay so that UI is maintained. Navigation might take
       sometime for the redirect page to load. This prevents UI teardown */
    setTimeout(() => {
      logout()
      setPageantId(null)
    }, 1500)
  }

  return (
    <div className="absolute right-4 top-4">
      <Button onClick={logoutAccount}>
        <LogOut />
        Logout
      </Button>
    </div>
  )
}
