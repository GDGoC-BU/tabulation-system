import { Link, Outlet, createFileRoute, redirect } from '@tanstack/react-router'
import { useAuthentication } from '@/features/authentication/store/use-authentication'
import { waitForStoreHydration } from '@/lib/wait-for-store-hydration'

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

const pageantActions = [
  {
    title: 'Pageants',
    url: '/admin/console/pageants',
  },
  {
    title: 'Judges',
    url: '/admin/console/judges',
  },
  {
    title: 'Candidates',
    url: '/admin/console/candidates',
  },
  {
    title: 'Phases',
    url: '/admin/console/phases',
  },
  {
    title: 'Segments',
    url: '/admin/console/segments',
  },
  {
    title: 'Colleges',
    url: '/admin/console/colleges',
  },
]

function ConsoleLayout() {
  return (
    <div className="flex flex-col divide-y gap-4">
      <div>
        <h1>LAYOUT</h1>
        <div className="flex flex-row gap-4">
          {pageantActions.map((action) => {
            return (
              <Link
                key={action.url}
                className="underline text-blue-500"
                to={action.url}
              >
                {action.title}
              </Link>
            )
          })}
        </div>
      </div>
      <div>
        <Outlet />
      </div>
    </div>
  )
}
