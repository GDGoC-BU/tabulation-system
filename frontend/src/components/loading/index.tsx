import { TextBody } from '../text'
import type { ComponentClassNameProp } from '@/types'
import { cn } from '@/lib/utils'

export default function Loading({ className }: ComponentClassNameProp) {
  return (
    <div className="flex flex-row items-center justify-center">
      <TextBody className="mr-[-2px]">Loading</TextBody>
      <div className={cn('bg-transparent', className)}>
        <img src="/spinner.svg" className="object-contain w-full" />
      </div>
    </div>
  )
}
