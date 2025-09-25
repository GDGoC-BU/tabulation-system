import { Outlet, createFileRoute, redirect } from '@tanstack/react-router'
import { useAuthentication } from '@/features/authentication/store/use-authentication'
import { waitForStoreHydration } from '@/lib/wait-for-store-hydration'
import { SidebarProvider } from '@/components/ui/sidebar'
import Navigation from '@/features/navigation/components'

export const Route = createFileRoute('/admin/console')({
  beforeLoad: async ({ context, location }) => {
    /* Wait for zustand to load from locale storage */
    await waitForStoreHydration(useAuthentication)
    /* If not authenticated, redirect to login */
    if (!context.authentication.isAuthenticated()) {
      throw redirect({
        to: '/admin/login',
        search: {
          /* But save the currenth location, so they can be redirected back here */
          redirect: location.href,
        },
      })
    }
  },
  component: ConsoleLayout,
})

function ConsoleLayout() {
  return (
    <SidebarProvider defaultOpen={true}>
      <Navigation.Admin.Vertical />
      <div className="w-full">
        <Navigation.Admin.Horizontal />
        <main>
          <Outlet />
        </main>
      </div>
    </SidebarProvider>
  )
}
