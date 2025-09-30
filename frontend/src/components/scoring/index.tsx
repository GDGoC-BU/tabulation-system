import { TextBody, TextHeading, textBodyClassName } from '../text'
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

function TabsFacade({
  className,
  children,
}: ComponentClassNameAndChildrenProp) {
  return (
    <div
      className={cn(
        'flex flex-col gap-2 max-w-[1080px] w-full mx-auto',
        className,
      )}
    >
      {children}
    </div>
  )
}
function TabsFacadeList({
  className,
  children,
}: ComponentClassNameAndChildrenProp) {
  return (
    <div
      className={cn(
        'bg-muted text-muted-foreground inline-flex h-9 w-full items-center justify-center rounded-lg p-[3px]',
        className,
      )}
    >
      {children}
    </div>
  )
}
function TabsFacadeListTrigger({
  className,
  children,
  active = false,
}: ComponentClassNameAndChildrenProp & { active?: boolean }) {
  const activeClassName =
    'bg-background text-foreground dark:border-input dark:bg-input/30 dark:text-muted-foreground shadow-sm'

  return (
    <div
      className={cn(
        "text-foreground inline-flex h-[calc(100%-1px)] flex-1 items-center justify-center gap-1.5 rounded-md border border-transparent px-2 py-1 text-sm font-medium whitespace-nowrap transition-[color,box-shadow] focus-visible:ring-[3px] focus-visible:outline-1 disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4",
        textBodyClassName,
        active && activeClassName,
        className,
      )}
    >
      {children}
    </div>
  )
}

function TabsFacadeBody({
  className,
  children,
}: ComponentClassNameAndChildrenProp) {
  return (
    <div className="bg-muted rounded-lg p-[3px] grow w-full">
      <div
        className={cn(
          'bg-background flex flex-col gap-2 p-4 rounded-md',
          className,
        )}
      >
        {children}
      </div>
    </div>
  )
}

function TabsFacadeBodyTitle({
  className,
  children,
}: ComponentClassNameAndChildrenProp) {
  return <TextHeading className={cn('', className)}>{children}</TextHeading>
}

function TabsFacadeBodyDescription({
  className,
  children,
}: ComponentClassNameAndChildrenProp) {
  return <TextBody className={cn('', className)}>{children}</TextBody>
}

function TabsFacadeBodyContent({
  className,
  children,
}: ComponentClassNameAndChildrenProp) {
  return (
    <div className={cn('grid grid-cols-2 gap-4', className)}>{children}</div>
  )
}

Header.Title = Title
Header.Sub = Sub
Scoring.Header = Header
TabsFacadeList.Trigger = TabsFacadeListTrigger
TabsFacade.List = TabsFacadeList
TabsFacadeBody.Title = TabsFacadeBodyTitle
TabsFacadeBody.Description = TabsFacadeBodyDescription
TabsFacadeBody.Content = TabsFacadeBodyContent
TabsFacade.Body = TabsFacadeBody
Scoring.TabsFacade = TabsFacade
Scoring.Content = Content
export default Scoring
