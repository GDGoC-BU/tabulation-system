import { useNavigate } from '@tanstack/react-router'
import { useSelectedPageant } from '../store/use-selected-pageant'
import type { PageantSummary } from '../schemas'
import type { ComponentClassNameAndChildrenProp } from '@/types'
import { TextBody } from '@/components/text'
import { cn } from '@/lib/utils'

export default function PageantTitle({
  className,
  children,
  pageant,
}: ComponentClassNameAndChildrenProp & { pageant: PageantSummary }) {
  const { setPageant } = useSelectedPageant((state) => state)
  const navigate = useNavigate()

  function onClick() {
    setPageant(pageant)
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
