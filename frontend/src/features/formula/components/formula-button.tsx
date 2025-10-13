import type { ComponentClassNameAndChildrenProp } from '@/types'
import type { buttonVariants } from '@/components/ui/button'
import type { VariantProps } from 'class-variance-authority'
import { Button } from '@/components/ui/button'
import { cn } from '@/lib/utils'

export default function FormulaButton({
  className,
  children,
  ...props
}: ComponentClassNameAndChildrenProp &
  React.ComponentProps<'button'> &
  VariantProps<typeof buttonVariants> & {
    asChild?: boolean
  }) {
  return (
    <Button
      type="button"
      variant="outline"
      className={cn('', className)}
      {...props}
    >
      {children}
    </Button>
  )
}
