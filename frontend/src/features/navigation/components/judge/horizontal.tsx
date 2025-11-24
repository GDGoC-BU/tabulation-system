import { useNavigate } from '@tanstack/react-router'
import { LogOut } from 'lucide-react'
import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'
import { useSelectedPageantIdStore } from '@/features/pageants/store/use-selected-pageant-id-store'
import { TextBody } from '@/components/text'
import { cn } from '@/lib/utils'

export default function Horizontal() {
  const [isLoggingOut, setIsLoggingOut] = useState(false)
  const { logout } = useAuthenticationStore((state) => state)
  const { setSelectedPageantId: setPageantId } = useSelectedPageantIdStore(
    (state) => state,
  )
  const navigation = useNavigate()

  function logoutAccount() {
    setIsLoggingOut(true)
    logout()
    setPageantId(null)
    setTimeout(() => {
      setIsLoggingOut(false)
      navigation({ to: '/' })
    }, 1500)
  }

  return (
    <>
      <div className="absolute right-4 top-4">
        <Button onClick={logoutAccount}>
          <LogOut />
          Logout
        </Button>
      </div>
      <div
        className={cn(
          'z-[9999] w-screen h-screen bg-background top-0 grid place-items-center',
          isLoggingOut ? 'fixed' : 'hidden',
        )}
      >
        <TextBody>Logging out...</TextBody>
      </div>
    </>
  )
}
