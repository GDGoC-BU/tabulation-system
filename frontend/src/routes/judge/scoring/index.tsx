import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/judge/scoring/')({
  component: JudgeScoring,
})

function JudgeScoring() {
  return <div>Hello "/judge/scoring/"!</div>
}
