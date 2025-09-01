import { cn } from '@/lib/utils'

type props = {
  className?: string
  children?: React.ReactNode
}

export const TextDisplay = ({ className, children }: props) => {
  return (
    <h1
      className={cn(
        'text-[80px] leading-[85px] font-[600] tracking-[-0.04em]',
        className
      )}
    >
      {children}
    </h1>
  )
}

export const TextHeading = ({ className, children }: props) => {
  return (
    <p
      className={cn(
        'text-[24px] leading-[28px] font-[700] tracking-[0]',
        className
      )}
    >
      {children}
    </p>
  )
}

export const TextBody = ({ className, children }: props) => {
  return (
    <p
      className={cn(
        'text-[15px] leading-[20px] font-[500] tracking-[0]',
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
        'text-muted-400 text-[13px] leading-[16px] font-[400] tracking-[0]',
        className
      )}
    >
      {children}
    </p>
  )
}
