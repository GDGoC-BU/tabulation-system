import { LinkButton, LinkText } from '@/components/link'
import { TextDisplay, TextHeading } from '@/components/text'

function Judge() {
  return (
    <div className='grid h-screen place-items-center'>
      <div className='flex flex-col items-center gap-4'>
        <div>
          <TextDisplay>Judge</TextDisplay>
        </div>
        <div className='flex flex-col items-center gap-4'>
          <LinkButton href='/judge/scoring'>Login</LinkButton>
          <LinkText href='/'>Home</LinkText>
        </div>
      </div>
    </div>
  )
}

export default Judge
