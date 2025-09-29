import { createFileRoute } from '@tanstack/react-router'
import { TabsTrigger } from '@radix-ui/react-tabs'
import { useEffect } from 'react'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'
import { usePageantQuery } from '@/features/pageants/hooks/use-pageant-query'
import Scoring from '@/components/scoring'
import { Tabs, TabsList } from '@/components/ui/tabs'
import { useSelectedPageantIdStore } from '@/features/pageants/store/use-selected-pageant-id-store'
import { useOngoingPhaseQuery } from '@/features/phases/hooks/use-ongoing-phase-query'
import { TextDisplay } from '@/components/text'

export const Route = createFileRoute('/judge/scoring/')({
  component: JudgeScoring,
})

function JudgeScoring() {
  const { account, getAssignedPageantId } = useAuthenticationStore()
  const { data: assignedPageant } = usePageantQuery(getAssignedPageantId())
  const { setSelectedPageantId } = useSelectedPageantIdStore()
  const { data: currentPhase } = useOngoingPhaseQuery()

  useEffect(() => {
    if (assignedPageant) {
      setSelectedPageantId(assignedPageant.id)
    }
  }, [assignedPageant])

  return (
    <Scoring>
      <Scoring.Header>
        <TextDisplay>{assignedPageant?.title}</TextDisplay>
        <Scoring.Header.Title>{currentPhase?.name}</Scoring.Header.Title>
        <Scoring.Header.Sub>Welcome, {account?.username}</Scoring.Header.Sub>
      </Scoring.Header>
      <Scoring.Content>
        <Tabs>
          <TabsList>
            <TabsTrigger value=""></TabsTrigger>
          </TabsList>
        </Tabs>
      </Scoring.Content>
    </Scoring>
  )
}
