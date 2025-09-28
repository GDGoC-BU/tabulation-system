import { createFileRoute } from '@tanstack/react-router'
import { useLoggedInAccountQuery } from '@/features/authentication/hooks/use-logged-in-account-query'

export const Route = createFileRoute('/judge/scoring/')({
  component: JudgeScoring,
})

function JudgeScoring() {
  const { data: judgeAccount } = useLoggedInAccountQuery()
  return <div>Hello "/judge/scoring/"!</div>
}
