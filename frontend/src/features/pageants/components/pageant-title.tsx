import { useNavigate } from '@tanstack/react-router'
import { useSelectedPageantId } from '../store/use-selected-pageant-id'
import type { PageantSummary } from '../schemas'
import type { ComponentClassNameAndChildrenProp } from '@/types'
import { TextBody } from '@/components/text'
import { cn } from '@/lib/utils'

export default function PageantTitle({
  className,
  children,
  pageant,
}: ComponentClassNameAndChildrenProp & { pageant: PageantSummary }) {
  const { setSelectedPageantId: setPageantId } = useSelectedPageantId(
    (state) => state,
  )
  const navigate = useNavigate()

  function onClick() {
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
