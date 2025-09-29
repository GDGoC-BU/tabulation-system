import { createFileRoute } from '@tanstack/react-router'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'

export const Route = createFileRoute('/judge/scoring/')({
  component: JudgeScoring,
})

function JudgeScoring() {
  const { getAccountId } = useAuthenticationStore()
  return <div>Hello "/judge/scoring/"!</div>
}
