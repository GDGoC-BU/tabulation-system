import { cn } from '@/lib/utils'

type props = {
  className?: string
  children?: React.ReactNode
}

export const textDisplayClassName =
  'font-heading text-[89.76px] leading-[85px] font-[600] tracking-[-0.05em]'
export const TextDisplay = ({ className, children }: props) => {
  return <h1 className={cn(textDisplayClassName, className)}>{children}</h1>
}

export const textHeadingClassName =
  'font-heading text-[28.43px] leading-[28px] font-[600] tracking-[0]'
export const TextHeading = ({ className, children }: props) => {
  return <h1 className={cn(textHeadingClassName, className)}>{children}</h1>
}

export const textBodyClassName =
  'font-body text-[16px] leading-[20px] font-[500] tracking-[-px]'
export const TextBody = ({ className, children }: props) => {
  return <p className={cn(textBodyClassName, className)}>{children}</p>
}

export const textSubClassName =
  'font-body  text-[12px] leading-[16px] font-[400] tracking-[0]'
export const TextSub = ({ className, children }: props) => {
  const forcedClassName = 'text-muted-foreground'
  return (
    <p className={cn(textSubClassName, forcedClassName, className)}>
      {children}
    </p>
  )
}
