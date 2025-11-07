import { Outlet, createFileRoute, redirect } from '@tanstack/react-router'
import { accountRoleSchema } from '@/features/authentication/schemas'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'
import { waitForStoreHydration } from '@/lib/wait-for-store-hydration'
import { useStompStore } from '@/store/stomp-store'
import Navigation from '@/features/navigation/components'
import Loading from '@/components/loading'

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
      console.log('Authenticated...')
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
  const { connect, isConnected } = useStompStore((state) => state)
  connect()

  if (!isConnected) {
    return (
      <div className="w-full h-screen grid place-items-center">
        <main>
          <Loading />
        </main>
      </div>
    )
  }

  return (
    <div className="relative">
      <Navigation.Judge.Horizontal />
      <main>
        <Outlet />
      </main>
    </div>
  )
}
