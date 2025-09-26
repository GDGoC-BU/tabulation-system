import { LinkDefault } from '@/components/link'
import { TextHeading } from '@/components/text'
import Authentication from '@/features/authentication/components'

export default function AdminLogin() {
  return (
    <div className='grid h-screen place-items-center'>
      <div className='flex flex-col items-center gap-4 rounded-lg border px-8 py-8'>
        <div>
          <TextHeading>Admin</TextHeading>
        </div>
        <div className='flex flex-col items-center gap-2'>
          <Authentication.Admin />
          <LinkDefault href='/'>Home</LinkDefault>
        </div>
      </div>
    </div>
  )
}
