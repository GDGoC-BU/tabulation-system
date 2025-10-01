import { Outlet, createFileRoute, redirect } from '@tanstack/react-router'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'
import { waitForStoreHydration } from '@/lib/wait-for-store-hydration'
import { SidebarProvider } from '@/components/ui/sidebar'
import Navigation from '@/features/navigation/components'
import { accountRoleSchema } from '@/features/authentication/schemas'

export const Route = createFileRoute('/admin/console')({
  beforeLoad: async ({ context, location }) => {
    /* Wait for zustand to load from locale storage */
    await waitForStoreHydration(useAuthenticationStore)

    const isAuthenticated = context.authentication.isAuthenticated()
    const currentLoggedInAccountRole = context.authentication.getAccountRole()

    /* If not authenticated or no role is assigned, redirect to login */
    if (!isAuthenticated || !currentLoggedInAccountRole) {
      throw redirect({
        to: '/admin/login',

        /* Redirect after login */
        search: {
          //  redirect: location.href,
          redirect: '/admin/console',
        },
      })
    }

    /* At this point they had valid credentials but is just using the wrong form.
       So just redirect them to their assign routes */

    if (currentLoggedInAccountRole === accountRoleSchema.enum.ADMIN) {
      return
    }

    if (currentLoggedInAccountRole === accountRoleSchema.enum.JUDGE) {
      throw redirect({
        to: '/judge/scoring',
      })
    }
  },
  component: AdminConsoleLayout,
})

function AdminConsoleLayout() {
  return (
    <SidebarProvider defaultOpen={true}>
      <Navigation.Admin.Vertical />
      <div className="w-full flex flex-col">
        <Navigation.Admin.Horizontal />
        <main className="grow">
          <Outlet />
        </main>
      </div>
    </SidebarProvider>
  )
}
