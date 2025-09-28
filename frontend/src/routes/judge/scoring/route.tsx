import { Outlet, createFileRoute, redirect } from '@tanstack/react-router'
import { accountRoleSchema } from '@/features/authentication/schemas'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'
import { waitForStoreHydration } from '@/lib/wait-for-store-hydration'

export const Route = createFileRoute('/judge/scoring')({
  beforeLoad: async ({ context, location }) => {
    /* Wait for zustand to load from locale storage */
    await waitForStoreHydration(useAuthenticationStore)

    const isAuthenticated = context.authentication.isAuthenticated()
    const currentLoggedInAccountRole = context.authentication.getAccountRole()

    /* If not authenticated or no role is assigned, redirect to login */
    if (!isAuthenticated || !currentLoggedInAccountRole) {
      throw redirect({
        to: '/judge/login',
        search: {
          /* But save the currenth location, so they can be redirected back here */
          redirect: location.href,
        },
      })
    }

    /* At this point they had valid credentials but is just using the wrong form.
           So just redirect them to their assign routes */

    if (currentLoggedInAccountRole === accountRoleSchema.enum.JUDGE) {
      return
    }

    if (currentLoggedInAccountRole === accountRoleSchema.enum.ADMIN) {
      throw redirect({
        to: '/admin/console',
      })
    }
  },
  component: JudgeScoringLayout,
})

function JudgeScoringLayout() {
  return <Outlet />
}
