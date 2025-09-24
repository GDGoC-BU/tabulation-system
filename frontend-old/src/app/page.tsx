import { LinkButton } from '@/components/link'
import { TextDisplay } from '@/components/text'

export default function Home() {
  return (
    <div className='grid h-screen place-items-center'>
      <div className='flex flex-col items-center gap-4'>
        <div>
          <TextDisplay>Tabulation</TextDisplay>
        </div>
        <div className='flex gap-4'>
          <LinkButton href='/judge/login'>Judge</LinkButton>

          <LinkButton href='/admin/login'>Admin</LinkButton>
        </div>
      </div>
    </div>
  )
}
