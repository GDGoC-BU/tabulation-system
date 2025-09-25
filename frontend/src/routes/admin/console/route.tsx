import { Link, Outlet, createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/console')({
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
              <Link className="underline text-blue-500" to={action.url}>
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
