import { LinkButton, LinkText } from '@/components/link'
import { TextDisplay } from '@/components/text'

function AdminLogin() {
  return (
    <div className='grid h-screen place-items-center'>
      <div className='flex flex-col items-center gap-4'>
        <div>
          <TextDisplay>Admin</TextDisplay>
        </div>
        <div className='flex flex-col items-center gap-4'>
          <LinkButton href='/admin/dashboard'>Login</LinkButton>
          <LinkText href='/'>Home</LinkText>
        </div>
      </div>
    </div>
  )
}

export default AdminLogin
