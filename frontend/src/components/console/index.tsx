import { cn } from '@/lib/utils'
import { TextHeading } from '../text'

function Console({ className, children }: ComponentClassNameAndChildrenProp) {
  return <div className={cn('p-4', className)}>{children}</div>
}

function Header({ className, children }: ComponentClassNameAndChildrenProp) {
  return (
    <div className={cn('mb-4', className)}>
      <TextHeading>{children}</TextHeading>
    </div>
  )
}

function Content({ className, children }: ComponentClassNameAndChildrenProp) {
  return <div className={cn('', className)}>{children}</div>
}

Console.Header = Header
Console.Content = Content
export default Console
