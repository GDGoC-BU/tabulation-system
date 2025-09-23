import { TextBody } from '@/components/text'
import { cn } from '@/lib/utils'
import { ComponentClassNameAndChildrenProp } from '@/types'
import React from 'react'
import { usePageant } from '../store/usePageant'
import { PageantSummary } from '../schemas/pageant'
import { useRouter } from 'next/navigation'

export default function PageantTitle({
  className,
  children,
  pageant
}: ComponentClassNameAndChildrenProp & { pageant: PageantSummary }) {
  const { setPageant } = usePageant(state => state)
  const router = useRouter()

  function onClick() {
    setPageant(pageant)
    router.push('/admin/console/dashboard')
  }

  return (
    /* Modify text component so that default html parameters can be passed directly */
    <div onClick={onClick}>
      <TextBody className={cn('underline hover:cursor-pointer', className)}>
        {children}
      </TextBody>
    </div>
  )
}
