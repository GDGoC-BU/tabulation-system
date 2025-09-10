import SideBar from './side-bar'

function Layout({ children }: ComponentChildrenProp) {
  return (
    <div className='grid min-h-screen w-full grid-cols-[300px_1fr] items-start gap-4'>
      <SideBar />
      <main>{children}</main>
    </div>
  )
}

export default Layout
