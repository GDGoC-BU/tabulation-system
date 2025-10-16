import { Link, createFileRoute } from '@tanstack/react-router'
import { useSoftResetQuery } from '@/features/pageants/hooks/use-soft-reset-query'
import { TextHeading } from '@/components/text'
import { Button } from '@/components/ui/button'

export const Route = createFileRoute(
  '/admin/console/pageants/$pageantId/soft-reset',
)({
  component: RouteComponent,
})

function RouteComponent() {
  const { pageantId } = Route.useParams()
  const { data: pageant } = useSoftResetQuery(pageantId)

  return (
    <div className="m-4 p-4 flex flex-col gap-4">
      <TextHeading>Soft Reset on {pageant?.title} complete!</TextHeading>
      <div>
        <Button asChild>
          <Link to="/admin/console/pageants">Go back</Link>
        </Button>
      </div>
    </div>
  )
}
