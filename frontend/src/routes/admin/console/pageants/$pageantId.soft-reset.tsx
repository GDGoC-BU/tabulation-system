import { Link, createFileRoute } from '@tanstack/react-router'
import { useEffect, useState } from 'react'
import type { PageantSummary } from '@/features/pageants/schemas'
import { useSoftResetMutate } from '@/features/pageants/hooks/use-soft-reset-mutate'
import { TextHeading } from '@/components/text'
import { Button } from '@/components/ui/button'

export const Route = createFileRoute(
  '/admin/console/pageants/$pageantId/soft-reset',
)({
  component: RouteComponent,
})

function RouteComponent() {
  const { pageantId } = Route.useParams()
  const [pageant, setPageant] = useState<PageantSummary | null>(null)
  const { mutateAsync: softResetPageant } = useSoftResetMutate(pageantId)

  useEffect(() => {
    const softReset = async () => {
      const data = await softResetPageant(pageantId)
      setPageant(data)
    }
    softReset()
  }, [])

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
