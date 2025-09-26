import { Button } from '@/components/ui/button'
import { SidebarTrigger } from '@/components/ui/sidebar'
import { cn } from '@/lib/utils'
import { Settings, Sun } from 'lucide-react'

export default function Horizontal({ className }: ComponentClassNameProp) {
  return (
    <div
      className={cn(
        'sticky top-0 flex h-[65px] flex-row justify-between border-b p-4 backdrop-blur-xl',
        className
      )}
    >
      <div className='flex flex-row items-center gap-4'>
        <SidebarTrigger className='' />
        <div className='h-full border-r' />
      </div>
      <div className='flex flex-row items-center gap-4'>
        <Button variant='ghost'>
          <Sun className='size-[18px]' />
        </Button>
        <Button variant='ghost'>
          <Settings className='size-[18px]' />
        </Button>
      </div>
    </div>
  )
}
