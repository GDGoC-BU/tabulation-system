import { createFileRoute } from '@tanstack/react-router'
import { useEffect, useMemo } from 'react'
import { useQuery } from '@tanstack/react-query'
import type { ScoreDetailed } from '@/features/scores/schemas'
import type { PhaseDetailed, PhaseHierarchy } from '@/features/phases/schemas'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'
import { useSelectedPageantIdStore } from '@/features/pageants/store/use-selected-pageant-id-store'
import Scoring from '@/components/scoring'
import { TextBody, TextDisplay } from '@/components/text'
import capitalizeWords from '@/lib/capitalize-words'
import judgeQueryOptions from '@/features/judges/query-options/judge-query-options'
import scoresQueryOptions from '@/features/scores/query-options/scores-query-options'
import splitCandidates from '@/features/candidates/lib/split-candidates'
import { useStompStore } from '@/store/stomp-store'
import { phaseSegmentStatusValue } from '@/schemas'
import CandidateScoreCard from '@/features/scores/components/candidate-score-card'
import pageantHierarchyQueryOptions from '@/features/pageants/query-options/pageant-hierarchy-query-options'

function ScoringTabsFacadeBodyNoActiveSegmentFallback({
  phase,
}: {
  phase: PhaseDetailed | PhaseHierarchy
}) {
  let title = 'No active segment'
  let body = 'Kindly wait for admin to start the next segment'

  const PENDING = phaseSegmentStatusValue.enum.PENDING
  const CLOSED = phaseSegmentStatusValue.enum.CLOSED

  if (phase.segments.every((segment) => segment.status === CLOSED)) {
    title = `${phase.name} has completed!`
    body = 'Thank you for participating in this pageant.'
  } else if (phase.segments.every((segment) => segment.status === PENDING)) {
    title = `${phase.name} has not started`
    body = 'Kindly wait for admin to start a segment'
  }

  return (
    <Scoring.TabsFacade.Body className="w-full h-[500px] grow grid place-items-center">
      <div className="flex flex-col text-center gap-2">
        <Scoring.TabsFacade.Body.Title>{title}</Scoring.TabsFacade.Body.Title>
        <Scoring.TabsFacade.Body.Description>
          {body}
        </Scoring.TabsFacade.Body.Description>
      </div>
    </Scoring.TabsFacade.Body>
  )
}

export const Route = createFileRoute('/judge/scoring/')({
  component: RouteComponent,
})

function RouteComponent() {
  /* 
  
  ============================== STEP 1 ============================== 
  
  */
  /* Get authenticated account and assigned pageant id */
  const { account, getAssignedPageantId } = useAuthenticationStore()

  /* Use the assigned pageant id to fetch the pageant hierarchy */
  const { data: assignedPageant, refetch: refetchAssignedPageant } = useQuery(
    pageantHierarchyQueryOptions(getAssignedPageantId(), {
      enabled: !!getAssignedPageantId(),
      staleTime: Infinity,
    }),
  )
  /* Set the selectedPageantId to attach Pageant-Id in the request headers 
     once the fetched pageant arrives.
     
     Note: Don't assign getAssignedPageantId() directly as we need to verify
     that the pageant actually exist in the backend. */
  const { isPageantSelected, setSelectedPageantId } =
    useSelectedPageantIdStore()
  useEffect(() => {
    if (assignedPageant) {
      setSelectedPageantId(assignedPageant.id)
    }
  }, [assignedPageant])

  /* Fetch judge only if there is an account logged in and the Pageant-Id
     has been set in the request headers */
  const { data: judge } = useQuery(
    judgeQueryOptions(account?.id, {
      enabled: !!account?.id && isPageantSelected,
    }),
  )

  /* 
  
  ============================== STEP 2 ============================== 
  
  */
  /* Get ongoing phase from the fetched pageant hierarchy */
  const ongoingPhase = useMemo(() => {
    if (!assignedPageant) return undefined
    return assignedPageant.phases.find(
      (phase) => phase.status === phaseSegmentStatusValue.enum.ONGOING,
    )
  }, [assignedPageant])

  /* Get ongoing segment from the fetched pageant hierarchy */
  const ongoingSegment = useMemo(() => {
    if (!ongoingPhase) return undefined
    return ongoingPhase.segments.find(
      (segment) => segment.status === phaseSegmentStatusValue.enum.ONGOING,
    )
  }, [ongoingPhase])

  /* Query the scores for the judge over the ongoing segment once the
     parameters are fetched */
  const { data: scores } = useQuery(
    scoresQueryOptions(
      {
        judgeId: judge?.id,
        segmentId: ongoingSegment?.id,
      },
      { enabled: !!judge?.id && !!ongoingSegment?.id },
    ),
  )

  /* Group scores by candidate in a map for faster lookup once scores
     are fetched */
  const candidateScoresMap = useMemo(() => {
    const map = new Map<string, Array<ScoreDetailed>>()
    if (!scores) return map

    scores.forEach((score) => {
      const key = score.candidateId
      if (!map.has(key)) {
        map.set(key, [])
      }
      map.get(key)?.push(score)
    })
    return map
  }, [scores])

  /* Get the qualified candidates and group by gender */
  const { femaleCandidates, maleCandidates } = useMemo(() => {
    if (!ongoingSegment) {
      return {
        femaleCandidates: [],
        maleCandidates: [],
      }
    }
    const candidates = ongoingSegment.candidateQualifications
      .filter((qualification) => qualification.isQualified)
      .map((qualification) => qualification.candidate)
    return splitCandidates(candidates)
  }, [ongoingSegment])

  /* 
  
  ============================== STEP 3 ============================== 
  
  */
  /* Subscribe to /topic/pageants/${id}/ongoing-segment to get notified 
     when the current ongoing segment changes.
     
     NOTE: Only subscribe after the pageant and judge has been fetched.
     This prevents unsynchronized data */
  useEffect(() => {
    if (!assignedPageant || !judge) return

    const { subscribe } = useStompStore.getState()
    const subscription = subscribe(
      `/topic/pageants/${assignedPageant.id}/ongoing-segment`,
      (message) => {
        try {
          const data = JSON.parse(message.body)
          refetchAssignedPageant()
        } catch (err) {
          console.error('Failed to parse STOMP message', err)
        }
      },
    )

    return () => {
      subscription?.unsubscribe()
    }
  }, [assignedPageant, judge, scores])

  /* Loading fallback */
  if (!assignedPageant || !judge || !ongoingPhase) {
    return (
      <Scoring className="w-full h-screen">
        <Scoring.Content className="grid place-items-center">
          <TextBody>Loading...</TextBody>
        </Scoring.Content>
      </Scoring>
    )
  }

  /* If there is no ongoing segment, display a fallback */
  const ScoringTabsFacadeBody = ongoingSegment ? (
    <Scoring.TabsFacade.Body>
      <Scoring.TabsFacade.Body.Title>
        {ongoingSegment.name}
      </Scoring.TabsFacade.Body.Title>
      <div className="">
        <Scoring.TabsFacade.Body.Description>
          Enter the scores for each candidate in the segment.
        </Scoring.TabsFacade.Body.Description>
        <Scoring.TabsFacade.Body.Description>
          {'->'} ❗Scores are saved automatically
        </Scoring.TabsFacade.Body.Description>
        <Scoring.TabsFacade.Body.Description>
          {'->'} ❗Scoring will automatically close when a segment finishes
        </Scoring.TabsFacade.Body.Description>
      </div>
      <Scoring.TabsFacade.Body.Content className="grid grid-cols-2">
        <div className="grid grid-cols-1 gap-4">
          {femaleCandidates.map((candidate) => {
            return (
              <CandidateScoreCard
                key={candidate.id}
                candidate={candidate}
                scores={candidateScoresMap.get(candidate.id)}
              />
            )
          })}
        </div>
        <div className="grid grid-cols-1 gap-4">
          {maleCandidates.map((candidate) => {
            return (
              <CandidateScoreCard
                key={candidate.id}
                candidate={candidate}
                scores={candidateScoresMap.get(candidate.id)}
              />
            )
          })}
        </div>
      </Scoring.TabsFacade.Body.Content>
    </Scoring.TabsFacade.Body>
  ) : (
    <ScoringTabsFacadeBodyNoActiveSegmentFallback phase={ongoingPhase} />
  )

  return (
    <Scoring>
      <Scoring.Header>
        <TextDisplay className="text-center">
          {assignedPageant.title}
        </TextDisplay>
        <Scoring.Header.Title>{ongoingPhase.name}</Scoring.Header.Title>
        <Scoring.Header.Sub>
          Welcome, {capitalizeWords(judge.honorific) + '.'}{' '}
          {capitalizeWords(judge.firstName)} {capitalizeWords(judge.lastName)}
        </Scoring.Header.Sub>
      </Scoring.Header>
      <Scoring.Content>
        <Scoring.TabsFacade>
          <Scoring.TabsFacade.List>
            {ongoingPhase.segments
              .sort((a, b) => a.sequence - b.sequence)
              .map((segment) => {
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
          {ScoringTabsFacadeBody}
        </Scoring.TabsFacade>
      </Scoring.Content>
    </Scoring>
  )
}
