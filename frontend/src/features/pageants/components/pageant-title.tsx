import { useNavigate } from '@tanstack/react-router'
import { useQueryClient } from '@tanstack/react-query'
import { useSelectedPageantIdStore } from '../store/use-selected-pageant-id-store'
import type { PageantSummary } from '../schemas'
import type { ComponentClassNameAndChildrenProp } from '@/types'
import { TextBody } from '@/components/text'
import { cn } from '@/lib/utils'

export default function PageantTitle({
  className,
  children,
  pageant,
}: ComponentClassNameAndChildrenProp & { pageant: PageantSummary }) {
  const queryClient = useQueryClient()
  const { setSelectedPageantId: setPageantId } = useSelectedPageantIdStore(
    (state) => state,
  )
  const navigate = useNavigate()

  function onClick() {
    /* Invalidate the pageant-related queries so it refetches the
       actual entities that belong to that pageant. */
    queryClient.invalidateQueries({ queryKey: ['judges'] })
    queryClient.invalidateQueries({ queryKey: ['candidates'] })
    queryClient.invalidateQueries({ queryKey: ['phases'] })
    queryClient.invalidateQueries({ queryKey: ['segments'] })
    setPageantId(pageant.id)
    navigate({ to: '/admin/console/dashboard' })
  }

  return (
    <TextBody
      onClick={onClick}
      className={cn('underline hover:cursor-pointer', className)}
    >
      {children}
    </TextBody>
  )
}
