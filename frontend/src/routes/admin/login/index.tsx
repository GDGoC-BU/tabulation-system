import { Link, createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/login/')({
  component: AdminLogin,
})

function AdminLogin() {
  return (
    <div>
      Hello "/admin/login/"! <Link to="/admin/console">GO TO CONSOLE</Link>
    </div>
  )
}
