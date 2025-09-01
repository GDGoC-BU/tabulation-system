import { TextDisplay, TextHeading } from '@/components/text'
import Link from '@/components/link'

export default function Home() {
  return (
    <div className='grid h-screen place-items-center'>
      <div className='flex flex-col items-center gap-4'>
        <div>
          <TextDisplay>Tabulation</TextDisplay>
        </div>
        <div className='flex gap-4'>
          <Link href='/judge'>
            <TextHeading>Judge</TextHeading>
          </Link>

          <Link href='/admin'>
            <TextHeading>Admin</TextHeading>
          </Link>
        </div>
      </div>
    </div>
  )
}
