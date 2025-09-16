import { SidebarProvider } from '@/components/ui/sidebar'
import Navigation from '@/features/navigation/components'

export default function layout({ children }: ComponentChildrenProp) {
  return (
    <SidebarProvider defaultOpen={true}>
      <Navigation.Admin.Vertical />
      <div className='w-full'>
        <Navigation.Admin.Horizontal className='p-4' />
        <main className='p-4'>{children}</main>
      </div>
    </SidebarProvider>
  )
}
