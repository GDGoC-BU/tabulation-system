import { LinkButton, LinkDefault } from '@/components/link'
import { TextDisplay } from '@/components/text'

export default function AdminLogin() {
  return (
    <div className='grid h-screen place-items-center'>
      <div className='flex flex-col items-center gap-4'>
        <div>
          <TextDisplay>Admin</TextDisplay>
        </div>
        <div className='flex flex-col items-center gap-4'>
          <LinkButton href='/admin/console'>Login</LinkButton>
          <LinkDefault href='/'>Home</LinkDefault>
        </div>
      </div>
    </div>
  )
}
