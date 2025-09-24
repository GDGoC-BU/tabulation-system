import { cn } from '@/lib/utils'
import { TextBody } from '../text'
import { Button } from '@/components/ui/button'
import Link from 'next/link'

type LinkProps = {
  href: string
}

export function LinkButton({
  className,
  children,
  href
}: ComponentClassNameAndChildrenProp & LinkProps) {
  return (
    <Button
      asChild
      variant='outline'
      className={cn('border hover:cursor-pointer', className)}
    >
      <Link href={href}>
        <TextBody>{children}</TextBody>
      </Link>
    </Button>
  )
}

export function LinkDefault({
  className,
  children,
  href
}: ComponentClassNameAndChildrenProp & LinkProps) {
  return (
    <Button asChild variant='link' className={cn('', className)}>
      <Link href={href}>
        <TextBody>{children}</TextBody>
      </Link>
    </Button>
  )
}
