import { cn } from '@/lib/utils'
import { ComponentClassNameAndChildrenProp } from '@/types'
import { TextHeading } from '../text'

function Console({ className, children }: ComponentClassNameAndChildrenProp) {
  return <div className={cn('p-4', className)}>{children}</div>
}

function Header({ className, children }: ComponentClassNameAndChildrenProp) {
  return <div className={cn('mb-4 flex flex-row', className)}>{children}</div>
}

function Title({ className, children }: ComponentClassNameAndChildrenProp) {
  return <TextHeading className={cn('', className)}>{children}</TextHeading>
}

function Content({ className, children }: ComponentClassNameAndChildrenProp) {
  return <div className={cn('', className)}>{children}</div>
}

Header.Title = Title
Console.Header = Header
Console.Content = Content
export default Console
