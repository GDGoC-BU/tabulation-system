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
import { useStompStore } from '@/store/stomp-store'
import { useSegmentQuery } from '@/features/segments/hooks/use-segment-query'

export const Route = createFileRoute('/judge/scoring/')({
  component: JudgeScoring,
})

function JudgeScoring() {
  const [currentSegments, setCurrentSegments] = useState<Segments>([])
  const [ongoingSegmentId, setOngoingSegmentId] = useState<string | null>(null)

  const { account, getAssignedPageantId } = useAuthenticationStore()
  const { data: assignedPageant } = usePageantQuery(getAssignedPageantId())
  const { setSelectedPageantId } = useSelectedPageantIdStore()
  const { data: currentPhase } = useOngoingPhaseQuery()
  const { data: ongoingSegment } = useSegmentQuery(ongoingSegmentId)

  /* Temporary hack! Backend can return segments for a phase */
  const { data: allSegments } = useSegmentsQuery()

  /* Subscribe to /topic/pageants/${id}/ongoing-segment
     To get the notified when the current ongoing segment change */
  useEffect(() => {
    if (!assignedPageant) return

    const { subscribe } = useStompStore.getState()

    const subscription = subscribe(
      `/topic/pageants/${assignedPageant.id}/ongoing-segment`,
      (message) => {
        try {
          const data = JSON.parse(message.body)
          setOngoingSegmentId(data?.id ?? null)
        } catch (err) {
          console.error('Failed to parse STOMP message', err)
        }
      },
    )

    return () => {
      subscription?.unsubscribe()
    }
  }, [assignedPageant])

  /* Set the selectedPageantId store to attach Pageant-Id in the request headers */
  useEffect(() => {
    if (assignedPageant) {
      setSelectedPageantId(assignedPageant.id)
    }
  }, [assignedPageant])

  /* Quick patch mentioned above: Filtering segments by the current phase. */
  useEffect(() => {
    if (allSegments && currentPhase) {
      const filtered = allSegments.filter((segment) => {
        if (segment.phase.id === currentPhase.id) {
          return segment
        }
      })
      setCurrentSegments(filtered)

      /* On initial load, get the ongoing segment. Websocket will only
           Ontify changes after subscription. */
      const ongoing = filtered.find(
        (segment) => segment.status === phaseSegmentStatusValue.enum.ONGOING,
      )
      if (ongoing) {
        console.log('ONGOING:', ongoing.name)
        setOngoingSegmentId(ongoing.id)
      }
    }
  }, [allSegments, currentPhase])

  const ScoringContent = ongoingSegment ? (
    <Scoring.TabsFacade.Body>
      <Scoring.TabsFacade.Body.Title>
        {ongoingSegment.name}
      </Scoring.TabsFacade.Body.Title>
      <Scoring.TabsFacade.Body.Description>
        Enter scores for each candidates in the segment
      </Scoring.TabsFacade.Body.Description>
      <Scoring.TabsFacade.Body.Content>
        {ongoingSegment.qualifiedCandidates.map((candidate) => {
          return (
            <Scoring.Card>
              <Scoring.Card.Header>
                <Scoring.Card.Header.Title>
                  Candidate {candidate.number}
                </Scoring.Card.Header.Title>
                <Scoring.Card.Header.BadgeGroup>
                  <Scoring.Card.Header.Badge>
                    {candidate.gender}
                  </Scoring.Card.Header.Badge>
                  <Scoring.Card.Header.Badge>
                    Total Score: 0
                  </Scoring.Card.Header.Badge>
                </Scoring.Card.Header.BadgeGroup>
              </Scoring.Card.Header>
              <Scoring.Card.Content>asfasf</Scoring.Card.Content>
            </Scoring.Card>
          )
        })}
      </Scoring.TabsFacade.Body.Content>
    </Scoring.TabsFacade.Body>
  ) : (
    <Scoring.TabsFacade.Body className="w-full h-[500px] grow grid place-items-center">
      <div className="flex flex-col text-center gap-2">
        <Scoring.TabsFacade.Body.Title>
          No ongoing segment.
        </Scoring.TabsFacade.Body.Title>
        <Scoring.TabsFacade.Body.Description>
          Summary of previous segment/s can be shown to the judges here.
        </Scoring.TabsFacade.Body.Description>
      </div>
    </Scoring.TabsFacade.Body>
  )

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
                  active={segment.id === ongoingSegmentId}
                >
                  {segment.name}
                </Scoring.TabsFacade.List.Trigger>
              )
            })}
          </Scoring.TabsFacade.List>
          {ScoringContent}
        </Scoring.TabsFacade>
      </Scoring.Content>
    </Scoring>
  )
}
