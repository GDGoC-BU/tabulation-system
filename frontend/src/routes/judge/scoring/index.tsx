import { createFileRoute } from '@tanstack/react-router'
import { useEffect, useMemo, useState } from 'react'
import type { Segments } from '@/features/segments/schemas'
import type { ScoreDetailed } from '@/features/scores/schemas'
import type { CandidateSummary } from '@/features/candidates/schemas'
import { candidateGender } from '@/features/candidates/schemas'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'
import { usePageantQuery } from '@/features/pageants/hooks/use-pageant-query'
import Scoring from '@/components/scoring'
import { useSelectedPageantIdStore } from '@/features/pageants/store/use-selected-pageant-id-store'
import { useOngoingPhaseQuery } from '@/features/phases/hooks/use-ongoing-phase-query'
import { TextBody, TextDisplay, TextSub } from '@/components/text'
import { useSegmentsQuery } from '@/features/segments/hooks/use-segments-query'
import { phaseSegmentStatusValue } from '@/schemas'
import { useStompStore } from '@/store/stomp-store'
import { useSegmentQuery } from '@/features/segments/hooks/use-segment-query'
import { useScoresQuery } from '@/features/scores/hooks/use-scores-query'
import { Input } from '@/components/ui/input'
import useDebounce from '@/hooks/use-debounce'
import useEditScoreMutate from '@/features/scores/hooks/use-edit-score-mutate'
import splitCandidates from '@/features/candidates/lib/split-candidates'

function ScoreInputForm({
  score,
  onChangeScore,
}: {
  score: ScoreDetailed
  onChangeScore: (id: string, value: number) => void
}) {
  const [scoreValue, setScoreValue] = useState(score.value)
  const [isError, setIsError] = useState(false)
  const debouncedScore = useDebounce(scoreValue, 2000)
  const { mutateAsync: editScore } = useEditScoreMutate()

  useEffect(() => {
    if (isNaN(debouncedScore) || debouncedScore > score.criterion.maxScore) {
      setIsError(true)
      return
    }
    setIsError(false)

    const changeScore = async () => {
      const updatedScore = await editScore({
        id: score.id,
        value: debouncedScore,
      })
      onChangeScore(score.id, updatedScore.value)
    }
    changeScore()
  }, [debouncedScore])

  return (
    <div className="flex flex-col items-center ">
      <div className="grid grid-cols-2 gap-4 items-center">
        <div className="justify-self-end">
          <TextBody className="">{score.criterion.name}</TextBody>
          {isError && (
            <TextSub className="text-destructive text-end">
              Invalid Score
            </TextSub>
          )}
        </div>
        <div className="justify-self-start flex flex-row gap-4 items-center">
          <div className="w-fit">
            <Input
              defaultValue={score.value}
              type="number"
              min={0}
              max={5}
              onChange={(e) => setScoreValue(parseInt(e.target.value))}
            />
          </div>
          <TextSub>Max: {score.criterion.maxScore}</TextSub>
        </div>
      </div>
    </div>
  )
}

function CandidateScoreCard({
  candidate,
  scores = [],
}: {
  candidate: CandidateSummary
  scores?: Array<ScoreDetailed>
}) {
  const [scoreValues, setScoreValues] = useState(() =>
    Object.fromEntries(scores.map((score) => [score.id, score.value])),
  )

  /* Prop drill and handle total score count client side to minimize expensive score fetching and keep it simple. */
  const handleScoreChange = (id: string, value: number) => {
    setScoreValues((prev) => ({
      ...prev,
      [id]: value,
    }))
  }

  const totalScore = Object.values(scoreValues).reduce(
    (sum, val) => sum + (isNaN(val) ? 0 : val),
    0,
  )

  /* Determine the badge for each gender */
  let GenderBadge = (
    <Scoring.Card.Header.Badge className="bg-purple-400 font-semibold">
      Mx.
    </Scoring.Card.Header.Badge>
  )
  if (candidate.gender === candidateGender.enum.FEMALE) {
    GenderBadge = (
      <Scoring.Card.Header.Badge className="bg-pink-400 font-semibold">
        Ms.
      </Scoring.Card.Header.Badge>
    )
  } else if (candidate.gender === candidateGender.enum.MALE) {
    GenderBadge = (
      <Scoring.Card.Header.Badge className="bg-blue-400 font-semibold">
        Mr.
      </Scoring.Card.Header.Badge>
    )
  }

  return (
    <Scoring.Card>
      <Scoring.Card.Header>
        <Scoring.Card.Header.Title>
          Candidate {candidate.number}
        </Scoring.Card.Header.Title>
        <Scoring.Card.Header.BadgeGroup>
          {GenderBadge}
          <Scoring.Card.Header.Badge>
            Total Score: {totalScore}
          </Scoring.Card.Header.Badge>
        </Scoring.Card.Header.BadgeGroup>
      </Scoring.Card.Header>
      <Scoring.Card.Content>
        <div className="flex flex-col gap-8">
          {scores.map((score) => {
            return (
              <ScoreInputForm
                key={score.id}
                score={score}
                onChangeScore={handleScoreChange}
              />
            )
          })}
        </div>
      </Scoring.Card.Content>
    </Scoring.Card>
  )
}

export const Route = createFileRoute('/judge/scoring/')({
  component: JudgeScoring,
})

function JudgeScoring() {
  const { account, getAssignedPageantId } = useAuthenticationStore()
  const { setSelectedPageantId } = useSelectedPageantIdStore()
  const { data: assignedPageant } = usePageantQuery(getAssignedPageantId())

  const [currentSegments, setCurrentSegments] = useState<Segments>([])
  const [ongoingSegmentId, setOngoingSegmentId] = useState<string | null>(null)

  const { data: currentPhase } = useOngoingPhaseQuery()
  const { data: ongoingSegment } = useSegmentQuery(ongoingSegmentId)
  const { data: scores } = useScoresQuery(
    {
      judgeId: account?.id,
      segmentId: ongoingSegmentId ?? '',
    },
    /* Only run the query when all parameters are available */
    !!account?.id && !!ongoingSegment?.id,
  )

  /* Group scores by candidate in a map for faster lookup */
  const scoresByCandidate = useMemo(() => {
    const map = new Map<string, Array<ScoreDetailed>>()
    if (!scores) return map

    scores.forEach((score) => {
      const key = score.candidateId
      if (!map.has(key)) map.set(key, [])
      map.get(key)?.push(score)
    })
    return map
  }, [scores])

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
           Notify changes after subscription. */
      const ongoing = filtered.find(
        (segment) => segment.status === phaseSegmentStatusValue.enum.ONGOING,
      )
      if (ongoing) {
        setOngoingSegmentId(ongoing.id)
      }
    }
  }, [allSegments, currentPhase])

  const { groupA: candidateGroupA, groupB: candidateGroupB } = useMemo(() => {
    if (!ongoingSegment) {
      return {
        groupA: [],
        groupB: [],
      }
    }

    return splitCandidates(ongoingSegment.qualifiedCandidates)
  }, [ongoingSegment])

  const ScoringContent = ongoingSegment ? (
    <Scoring.TabsFacade.Body>
      <Scoring.TabsFacade.Body.Title>
        {ongoingSegment.name}
      </Scoring.TabsFacade.Body.Title>
      <div className="">
        <Scoring.TabsFacade.Body.Description>
          Enter the scores for each candidate in the segment.
        </Scoring.TabsFacade.Body.Description>
        <Scoring.TabsFacade.Body.Description>
          Note: Scores are saved automatically, just wait for the admin to
          change the segment
        </Scoring.TabsFacade.Body.Description>
      </div>
      <Scoring.TabsFacade.Body.Content className="grid grid-cols-2">
        <div className="grid grid-cols-1 gap-4">
          {candidateGroupA.map((candidate) => {
            return (
              <CandidateScoreCard
                key={candidate.id}
                candidate={candidate}
                scores={scoresByCandidate.get(candidate.id)}
              />
            )
          })}
        </div>
        <div className="grid grid-cols-1 gap-4">
          {candidateGroupB.map((candidate) => {
            return (
              <CandidateScoreCard
                key={candidate.id}
                candidate={candidate}
                scores={scoresByCandidate.get(candidate.id)}
              />
            )
          })}
        </div>
      </Scoring.TabsFacade.Body.Content>
    </Scoring.TabsFacade.Body>
  ) : (
    /* Temporary body for inactive segments. Special cases apply
       when all segments are PENDING and CLOSED  */
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
