import { createFileRoute } from '@tanstack/react-router'
import { TextBody, TextDisplay, TextHeading, TextSub } from '@/components/text'

export const Route = createFileRoute('/')({
  component: App,
})

function App() {
  return (
    <div>
      <TextDisplay>Display</TextDisplay>
      <TextHeading>Heading</TextHeading>
      <TextBody>Body</TextBody>
      <TextSub>Sub</TextSub>
    </div>
  )
}
