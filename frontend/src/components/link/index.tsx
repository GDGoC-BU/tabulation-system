import { cn } from '@/lib/utils'
import NextLink from 'next/link'
import { TextBody, TextHeading } from '../text'

export function LinkButton({
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
      <TextHeading>{children}</TextHeading>
    </NextLink>
  )
}

export function LinkText({
  className,
  children,
  href,
  ...props
}: ComponentClassNameAndChildrenProp & { href: string }) {
  return (
    <NextLink
      className={cn(className, 'underline hover:cursor-pointer')}
      href={href}
      {...props}
    >
      <TextBody>{children}</TextBody>
    </NextLink>
  )
}
