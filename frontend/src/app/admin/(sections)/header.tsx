import { TextHeading } from '@/components/text'
import React from 'react'

export default function Header({ children }: ComponentChildrenProp) {
  return (
    <div className='sticky top-0 w-full bg-red-200 py-4'>
      <TextHeading>{children}</TextHeading>
    </div>
  )
}
