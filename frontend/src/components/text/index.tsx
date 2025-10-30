import type { ComponentClassNameAndChildrenProp } from '@/types'
import { cn } from '@/lib/utils'

export const textDisplayClassName =
  'font-heading text-[67.34px] leading-[85px] font-[600] tracking-[-0.05em]'
export const TextDisplay = ({
  className,
  children,
  ...props
}: ComponentClassNameAndChildrenProp &
  React.HTMLAttributes<HTMLHeadingElement>) => {
  return (
    <h1 {...props} className={cn(textDisplayClassName, className)}>
      {children}
    </h1>
  )
}

export const textHeadingClassName =
  'font-heading text-[28.43px] leading-[28px] font-[600] tracking-[0]'
export const TextHeading = ({
  className,
  children,
  ...props
}: ComponentClassNameAndChildrenProp &
  React.HTMLAttributes<HTMLHeadingElement>) => {
  return (
    <h1 {...props} className={cn(textHeadingClassName, className)}>
      {children}
    </h1>
  )
}

export const textBodyClassName =
  'font-body text-[16px] leading-[20px] font-[500] tracking-[-px]'
export const TextBody = ({
  className,
  children,
  ...props
}: ComponentClassNameAndChildrenProp &
  React.HTMLAttributes<HTMLParagraphElement>) => {
  return (
    <p {...props} className={cn(textBodyClassName, className)}>
      {children}
    </p>
  )
}

export const textSubClassName =
  'font-body  text-[12px] leading-[16px] font-[400] tracking-[0]'
export const TextSub = ({
  className,
  children,
  ...props
}: ComponentClassNameAndChildrenProp &
  React.HTMLAttributes<HTMLParagraphElement>) => {
  const forcedClassName = 'text-muted-foreground'
  return (
    <p {...props} className={cn(textSubClassName, forcedClassName, className)}>
      {children}
    </p>
  )
}
