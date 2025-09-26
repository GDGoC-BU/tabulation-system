import { Link, createFileRoute } from '@tanstack/react-router'
import { TextDisplay } from '@/components/text'
import { Button } from '@/components/ui/button'

export const Route = createFileRoute('/')({
  component: App,
})

function App() {
  return (
    <div className="grid h-screen place-items-center">
      <div className="flex flex-col items-center gap-4">
        <div>
          <TextDisplay>Tabulation</TextDisplay>
        </div>
        <div className="flex gap-4">
          <Button asChild>
            <Link to="/judge/login">Judge</Link>
          </Button>
          <Button asChild>
            <Link to="/admin/login">Admin</Link>
          </Button>
        </div>
      </div>
    </div>
  )
}
