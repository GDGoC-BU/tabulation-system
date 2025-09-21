import Console from '@/components/console'
import { getPageants } from '@/features/pageants/actions/getPageants'

export default async function AdminPageants() {
  const pageants = await getPageants()
  console.log(pageants)

  return (
    <Console>
      <Console.Header>
        <Console.Header.Title>Pageants</Console.Header.Title>
      </Console.Header>
      <Console.Content></Console.Content>
    </Console>
  )
}
