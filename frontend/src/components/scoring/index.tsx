import { TextBody, TextHeading } from '../text'
import type { ComponentClassNameAndChildrenProp } from '@/types'
import { cn } from '@/lib/utils'

function Scoring({ children, className }: ComponentClassNameAndChildrenProp) {
  return (
    <div className={cn('p-8 flex flex-col gap-4 min-h-screen', className)}>
      {children}
    </div>
  )
}

function Header({ className, children }: ComponentClassNameAndChildrenProp) {
  return (
    <div className={cn('flex flex-col items-center gap-2', className)}>
      {children}
    </div>
  )
}

function Title({ className, children }: ComponentClassNameAndChildrenProp) {
  return <TextHeading className={cn('', className)}>{children}</TextHeading>
}

function Sub({ className, children }: ComponentClassNameAndChildrenProp) {
  return <TextBody className={cn('', className)}>{children}</TextBody>
}

function Content({ className, children }: ComponentClassNameAndChildrenProp) {
  return <div className={cn('grow', className)}>{children}</div>
}

Header.Title = Title
Header.Sub = Sub
Scoring.Header = Header
Scoring.Content = Content
export default Scoring
