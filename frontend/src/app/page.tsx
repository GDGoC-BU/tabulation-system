import { TextDisplay, TextHeading } from '@/components/text'
import { LinkButton } from '@/components/link'

export default function Home() {
  return (
    <div className='grid h-screen place-items-center'>
      <div className='flex flex-col items-center gap-4'>
        <div>
          <TextDisplay>Tabulation</TextDisplay>
        </div>
        <div className='flex gap-4'>
          <LinkButton href='/judge'>Judge</LinkButton>

          <LinkButton href='/admin'>Admin</LinkButton>
        </div>
      </div>
    </div>
  )
}
