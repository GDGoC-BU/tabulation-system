import { cn } from '@/lib/utils'

type props = {
  className?: string
  children?: React.ReactNode
}

export const TextDisplay = ({ className, children }: props) => {
  return (
    <h1
      className={cn(
        'font-heading text-foreground text-[89.76px] leading-[85px] font-[600] tracking-[-0.05em]',
        className
      )}
    >
      {children}
    </h1>
  )
}

export const TextHeading = ({ className, children }: props) => {
  return (
    <h1
      className={cn(
        'font-heading text-foreground text-[28.43px] leading-[28px] font-[600] tracking-[0]',
        className
      )}
    >
      {children}
    </h1>
  )
}

export const TextBody = ({ className, children }: props) => {
  return (
    <p
      className={cn(
        'font-body text-foreground text-[16px] leading-[20px] font-[500] tracking-[-px]',
        className
      )}
    >
      {children}
    </p>
  )
}

export const TextSub = ({ className, children }: props) => {
  return (
    <p
      className={cn(
        'font-body text-muted-foreground text-[12px] leading-[16px] font-[400] tracking-[0]',
        className
      )}
    >
      {children}
    </p>
  )
}
