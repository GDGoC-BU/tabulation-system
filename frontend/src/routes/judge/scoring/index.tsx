import { createFileRoute } from '@tanstack/react-router'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'
import { usePageantQuery } from '@/features/pageants/hooks/use-pageant-query'
import { TextBody } from '@/components/text'

export const Route = createFileRoute('/judge/scoring/')({
  component: JudgeScoring,
})

function JudgeScoring() {
  const { account, getAssignedPageantId } = useAuthenticationStore()
  const { data: assignedPageant } = usePageantQuery(getAssignedPageantId())
  return (
    <div>
      <TextBody>
        {account?.username} : {assignedPageant?.title}
      </TextBody>
    </div>
  )
}
