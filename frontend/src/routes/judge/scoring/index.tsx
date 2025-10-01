import { createFileRoute } from '@tanstack/react-router'
import { useEffect, useState } from 'react'
import {
  DialogClose,
  DialogDescription,
  DialogTitle,
} from '@radix-ui/react-dialog'
import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import type { Segments } from '@/features/segments/schemas'
import type {
  ScoreDetailed,
  ScoreEditFormSchema,
} from '@/features/scores/schemas'
import type { CriterionSummary } from '@/features/criteria/schemas'
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
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { scoreEditFormSchema } from '@/features/scores/schemas'

function CandidateScoreCard({
  candidate,
  scores = [],
}: {
  candidate: CandidateSummary
  scores?: Array<ScoreDetailed>
}) {
  const form = useForm<ScoreEditFormSchema>({
    resolver: zodResolver(scoreEditFormSchema),
    defaultValues: {
      value: 0,
    },
  })

  function onScoreChange(values: ScoreEditFormSchema) {
    console.log('Score Values: ', values)
  }

  let genderBadgeColorClassName = 'bg-purple-400'
  if (candidate.gender === candidateGender.enum.FEMALE) {
    genderBadgeColorClassName = 'bg-pink-400'
  } else if (candidate.gender === candidateGender.enum.MALE) {
    genderBadgeColorClassName = 'bg-blue-400'
  }

  return (
    <Scoring.Card>
      <Scoring.Card.Header>
        <Scoring.Card.Header.Title>
          Candidate {candidate.number}
        </Scoring.Card.Header.Title>
        <Scoring.Card.Header.BadgeGroup>
          <Scoring.Card.Header.Badge className={genderBadgeColorClassName}>
            {candidate.gender}
          </Scoring.Card.Header.Badge>
          <Scoring.Card.Header.Badge>Total Score: 0</Scoring.Card.Header.Badge>
        </Scoring.Card.Header.BadgeGroup>
      </Scoring.Card.Header>
      <Scoring.Card.Content>
        <div className="flex flex-col gap-8">
          {scores.map((score) => {
            return (
              <div className="grid grid-cols-2 gap-4 items-center justify-center">
                <div className="grow text-end">
                  <TextBody>{score.criterion.name}</TextBody>
                </div>
                <div className="flex flex-row gap-4 items-center">
                  <div className="">
                    <Input
                      type="number"
                      max={score.criterion.maxScore}
                      min={0}
                      className="max-w-[100px]"
                    />
                  </div>
                  <div className="">
                    <TextSub>Max: {score.criterion.maxScore}</TextSub>
                  </div>
                </div>
              </div>
            )
          })}
        </div>
      </Scoring.Card.Content>
    </Scoring.Card>
  )
}

{
  /* <Form {...form}>
          <form
            onSubmit={form.handleSubmit(onScoreChange)}
            className="space-y-4"
          >
            <FormField
              control={form.control}
              name="value"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Value</FormLabel>
                  <FormControl>
                    <Input {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            {isError && <TextSub className="text-destructive">{error}</TextSub>}
          </form>
        </Form> */
}

export const Route = createFileRoute('/judge/scoring/')({
  component: JudgeScoring,
})

function JudgeScoring() {
  const [currentSegments, setCurrentSegments] = useState<Segments>([])
  const [ongoingSegmentId, setOngoingSegmentId] = useState<string | null>(null)

  const { account, getAssignedPageantId, getAccountId } =
    useAuthenticationStore()
  const { data: assignedPageant } = usePageantQuery(getAssignedPageantId())
  const { setSelectedPageantId } = useSelectedPageantIdStore()
  const { data: currentPhase } = useOngoingPhaseQuery()
  const { data: ongoingSegment } = useSegmentQuery(ongoingSegmentId)
  const { data: scores } = useScoresQuery(
    {
      judgeId: account?.id,
      segmentId: ongoingSegmentId ?? '',
    },
    !!account?.id && !!ongoingSegment?.id,
  )

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
        {ongoingSegment.qualifiedCandidates
          .sort((a, b) => a.number - b.number)
          .map((candidate) => {
            return (
              <CandidateScoreCard
                key={candidate.id}
                candidate={candidate}
                scores={scores?.filter(
                  (score) => score.candidateId === candidate.id,
                )}
              />
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
