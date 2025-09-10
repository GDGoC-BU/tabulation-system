import { LinkText } from '@/components/link'
import { TextBody } from '@/components/text'

function SideBar() {
  return (
    <nav className='sticky top-0 border-r bg-blue-200'>
      <div className=''>
        <LinkText href=''>Dashboard</LinkText>
        <LinkText href=''>Candidates</LinkText>
        <LinkText href=''>Judges</LinkText>
        <LinkText href=''>Colleges</LinkText>
      </div>
    </nav>
  )
}

export default SideBar
