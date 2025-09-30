import { createFileRoute } from '@tanstack/react-router'
import { useEffect, useState } from 'react'
import type { Segments } from '@/features/segments/schemas'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'
import { usePageantQuery } from '@/features/pageants/hooks/use-pageant-query'
import Scoring from '@/components/scoring'
import { useSelectedPageantIdStore } from '@/features/pageants/store/use-selected-pageant-id-store'
import { useOngoingPhaseQuery } from '@/features/phases/hooks/use-ongoing-phase-query'
import { TextDisplay } from '@/components/text'
import { useSegmentsQuery } from '@/features/segments/hooks/use-segments-query'
import { phaseSegmentStatusValue } from '@/schemas'
import { useOngoingSegmentId } from '@/features/segments/hooks/use-ongoing-segment-id'

export const Route = createFileRoute('/judge/scoring/')({
  component: JudgeScoring,
})

function JudgeScoring() {
  const [currentSegments, setCurrentSegments] = useState<Segments>([])

  const { account, getAssignedPageantId } = useAuthenticationStore()
  const { data: assignedPageant } = usePageantQuery(getAssignedPageantId())
  const { setSelectedPageantId } = useSelectedPageantIdStore()
  const { data: currentPhase } = useOngoingPhaseQuery()
  /* Temporary hack! Backend can return segments for a phase */
  const { data: allSegments } = useSegmentsQuery()

  /* Subscribe to /topic/pageants/${id}/ongoing-segment
     To get the notified when the current ongoing segment change */
  const ongoingSegmentId = useOngoingSegmentId(assignedPageant?.id)

  /* Set the selectedPageantId store to attach Pageant-Id in the request headers */
  useEffect(() => {
    if (assignedPageant) {
      setSelectedPageantId(assignedPageant.id)
    }
  }, [assignedPageant])

  /* Quick patch mentioned above. Filter segments by the current phase. */
  useEffect(() => {
    if (allSegments && currentPhase) {
      const filtered = allSegments.filter((segment) => {
        if (segment.phase.id === currentPhase.id) {
          return segment
        }
      })
      setCurrentSegments(filtered)
    }
  }, [allSegments, currentPhase])

  return (
    <Scoring>
      <Scoring.Header>
        <TextDisplay>{assignedPageant?.title}</TextDisplay>
        <Scoring.Header.Title>{currentPhase?.name}</Scoring.Header.Title>
        <Scoring.Header.Sub>Welcome, {account?.username}</Scoring.Header.Sub>
      </Scoring.Header>
      <Scoring.Content>
        <Scoring.TabsFacade>
          <Scoring.TabsFacade.List>
            {currentSegments.map((segment) => {
              return (
                <Scoring.TabsFacade.List.Trigger
                  key={segment.id}
                  active={
                    segment.status === phaseSegmentStatusValue.enum.ONGOING
                  }
                >
                  {segment.name}
                </Scoring.TabsFacade.List.Trigger>
              )
            })}
          </Scoring.TabsFacade.List>
          <Scoring.TabsFacade.Body>
            <Scoring.TabsFacade.Body.Title>
              Swimwear {ongoingSegmentId}
            </Scoring.TabsFacade.Body.Title>
            <Scoring.TabsFacade.Body.Description>
              Enter scores for each contestant in the segment
            </Scoring.TabsFacade.Body.Description>
            <Scoring.TabsFacade.Body.Content>
              <div className="p-4 rounded-lg border">1</div>
              <div className="p-4 rounded-lg border">1</div>
              <div className="p-4 rounded-lg border">2</div>
              <div className="p-4 rounded-lg border">2</div>
              <div className="p-4 rounded-lg border">3</div>
              <div className="p-4 rounded-lg border">3</div>
            </Scoring.TabsFacade.Body.Content>
          </Scoring.TabsFacade.Body>
        </Scoring.TabsFacade>
      </Scoring.Content>
    </Scoring>
  )
}
