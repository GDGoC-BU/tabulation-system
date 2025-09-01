import { cn } from '@/lib/utils'
import NextLink from 'next/link'

function Link({
  className,
  children,
  href,
  ...props
}: ComponentClassNameAndChildrenProp & { href: string }) {
  return (
    <NextLink
      className={cn(
        className,
        'border px-8 py-4 transition-transform hover:translate-y-[-5px] hover:cursor-pointer'
      )}
      href={href}
      {...props}
    >
      {children}
    </NextLink>
  )
}

export default Link
