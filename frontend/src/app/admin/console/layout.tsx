import { LinkDefault } from '@/components/link'

export default function layout({ children }: ComponentChildrenProp) {
  return (
    /* Spans whole width */
    <div className='bg-orange-400'>
      {/* Limit dashboard width */}
      <div className='m-auto flex h-screen w-full max-w-[1440px] flex-row items-stretch gap-4 bg-yellow-500'>
        {/* Sidebar */}
        <nav className='flex min-w-[300px] flex-col justify-between bg-blue-200'>
          <div className='flex flex-col'>
            <LinkDefault href='/admin/console/dashboard'>Dashboard</LinkDefault>
            <LinkDefault href='/admin/console/candidates'>
              Candidates
            </LinkDefault>
            <LinkDefault href='/admin/console/judges'>Judges</LinkDefault>
            <LinkDefault href='/admin/console/colleges'>Colleges</LinkDefault>
          </div>
          <div>
            <LinkDefault href=''>Bottom</LinkDefault>
          </div>
        </nav>
        {/* Content */}
        <main className='grow overflow-y-scroll bg-red-500'>{children}</main>
      </div>
    </div>
  )
}
