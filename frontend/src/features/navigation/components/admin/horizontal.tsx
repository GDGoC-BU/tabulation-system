import { LogOut, Settings, Sun } from 'lucide-react'
import { useNavigate } from '@tanstack/react-router'
import type { ComponentClassNameProp } from '@/types'
import { Button } from '@/components/ui/button'
import { SidebarTrigger } from '@/components/ui/sidebar'
import { cn } from '@/lib/utils'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'
import { useSelectedPageantIdStore } from '@/features/pageants/store/use-selected-pageant-id-store'

export default function Horizontal({ className }: ComponentClassNameProp) {
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
    }, 200)
  }
  return (
    <div
      className={cn(
        'sticky top-0 flex h-[65px] flex-row justify-between border-b p-4 backdrop-blur-xl',
        className,
      )}
    >
      <div className="flex flex-row items-center gap-4">
        <SidebarTrigger className="" />
        <div className="h-full border-r" />
      </div>
      <div className="flex flex-row items-center gap-4">
        <Button variant="ghost">
          <Sun className="size-[18px]" />
        </Button>
        <Button variant="ghost">
          <Settings className="size-[18px]" />
        </Button>
        <Button onClick={logoutAccount}>
          <LogOut />
          Logout
        </Button>
      </div>
    </div>
  )
}
