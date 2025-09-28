import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/judge/login/')({
  component: JudgeLogin,
})

function JudgeLogin() {
  return <div>Hello "/judge/login/"!</div>
}
