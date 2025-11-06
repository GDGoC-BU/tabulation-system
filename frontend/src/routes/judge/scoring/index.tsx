import { createFileRoute } from '@tanstack/react-router'
import { useCallback, useEffect, useMemo, useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import useEmblaCarousel from 'embla-carousel-react'
import { WheelGesturesPlugin } from 'embla-carousel-wheel-gestures'
import type { ScoreDetailed } from '@/features/scores/schemas'
import type { PhaseDetailed, PhaseHierarchy } from '@/features/phases/schemas'
import type {
  CandidateDetailed,
  CandidateGender,
  CandidateHierarchy,
  CandidateSummary,
} from '@/features/candidates/schemas'
import { candidateGender } from '@/features/candidates/schemas'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'
import { useSelectedPageantIdStore } from '@/features/pageants/store/use-selected-pageant-id-store'
import Scoring from '@/components/scoring'
import { TextBody, TextHeading, TextSub } from '@/components/text'
import capitalizeWords from '@/lib/capitalize-words'
import judgeQueryOptions from '@/features/judges/query-options/judge-query-options'
import scoresQueryOptions from '@/features/scores/query-options/scores-query-options'
import splitCandidates from '@/features/candidates/lib/split-candidates'
import { useStompStore } from '@/store/stomp-store'
import { phaseSegmentStatusValue } from '@/schemas'
import CandidateScoreCard from '@/features/scores/components/candidate-score-card'
import pageantHierarchyQueryOptions from '@/features/pageants/query-options/pageant-hierarchy-query-options'
import { cn } from '@/lib/utils'
import { Badge } from '@/components/ui/badge'

function ScoringTabsFacadeBodyNoActiveSegmentFallback({
  phase,
}: {
  phase: PhaseDetailed | PhaseHierarchy
}) {
  let title = 'No active segment'
  let body = '👨🏻‍💻Kindly wait for the admin to start the next segment'

  const PENDING = phaseSegmentStatusValue.enum.PENDING
  const CLOSED = phaseSegmentStatusValue.enum.CLOSED

  if (phase.segments.every((segment) => segment.status === CLOSED)) {
    title = `${phase.name} has completed!`
    body = '💖Thank you for participating in this pageant.'
  } else if (phase.segments.every((segment) => segment.status === PENDING)) {
    title = `${phase.name} has not started`
    body = `✨Kindly wait for the admin to start the pageant`
  }

  return (
    <Scoring.TabsFacade.Body className="w-full h-full grow grid place-items-center">
      <div className="flex flex-col text-center gap-2">
        <TextHeading>{title}</TextHeading>
        <TextBody>{body}</TextBody>
      </div>
    </Scoring.TabsFacade.Body>
  )
}

function CandidateScoringCards({
  candidates,
  scoresMap,
  gender,
}: {
  candidates: Array<CandidateDetailed | CandidateSummary | CandidateHierarchy>
  scoresMap: Map<string, Array<ScoreDetailed>>
  gender: CandidateGender
}) {
  const [emblaRef, emblaApi] = useEmblaCarousel({ loop: true }, [
    WheelGesturesPlugin(),
  ])

  const scrollPrev = useCallback(() => {
    if (emblaApi) emblaApi.scrollPrev()
  }, [emblaApi])

  const scrollNext = useCallback(() => {
    if (emblaApi) emblaApi.scrollNext()
  }, [emblaApi])

  const FEMALE = candidateGender.enum.FEMALE
  const MALE = candidateGender.enum.MALE

  let genderBadgeClassNames = ''
  if (gender === FEMALE) {
    genderBadgeClassNames = 'bg-gender-female-primary'
  } else if (gender === MALE) {
    genderBadgeClassNames = 'bg-gender-male-primary'
  }

  let genderShadowClassNames = ''
  if (gender === FEMALE) {
    genderShadowClassNames = 'shadow-lg shadow-gender-female-secondary'
  } else if (gender === MALE) {
    genderShadowClassNames = 'shadow-lg shadow-gender-male-secondary'
  }

  let genderRingClassNames = ''
  if (gender === FEMALE) {
    genderRingClassNames = 'ring-gender-female-primary'
  } else if (gender === MALE) {
    genderRingClassNames = 'ring-gender-male-primary'
  }

  let genderLabel = ''
  if (gender === FEMALE) {
    genderLabel = 'Female'
  } else if (gender === MALE) {
    genderLabel = 'Male'
  }

  const [selectedIndex, setSelectedIndex] = useState(0)
  const [emblaThumbsRef, emblaThumbsApi] = useEmblaCarousel({
    containScroll: 'keepSnaps',
    dragFree: true,
    active: false,
  })

  const onThumbClick = useCallback(
    (index: number) => {
      if (!emblaApi || !emblaThumbsApi) return
      emblaApi.scrollTo(index)
    },
    [emblaApi, emblaThumbsApi],
  )

  const onSelect = useCallback(() => {
    if (!emblaApi || !emblaThumbsApi) return
    setSelectedIndex(emblaApi.selectedScrollSnap())
    emblaThumbsApi.scrollTo(emblaApi.selectedScrollSnap())
  }, [emblaApi, emblaThumbsApi, setSelectedIndex])

  useEffect(() => {
    if (!emblaApi) return
    onSelect()

    emblaApi.on('select', onSelect).on('reInit', onSelect)
  }, [emblaApi, onSelect])

  return (
    <div className="overflow-hidden bg-muted flex flex-col gap-8 rounded-xl py-8 px-4 relative">
      <Badge className={cn('mx-auto', genderBadgeClassNames)}>
        <TextBody className="text-background">
          {genderLabel} Candidates
        </TextBody>
      </Badge>
      <div className="embla">
        <div className="embla__viewport relative" ref={emblaRef}>
          <div className="embla__container flex flex-row">
            {candidates.map((candidate) => {
              return (
                <CandidateScoreCard
                  className={cn('embla__slide mx-2', genderShadowClassNames)}
                  key={candidate.id}
                  candidate={candidate}
                  scores={scoresMap.get(candidate.id) ?? []}
                />
              )
            })}
          </div>
          <div
            onClick={scrollPrev}
            className="absolute w-[calc(26%)] top-0 bottom-0 left-[-1%] bg-tranparent hover:cursor-pointer"
          />
          <div
            onClick={scrollNext}
            className="absolute w-[calc(26%)] top-0 bottom-0 right-[-1%] bg-tranparent hover:cursor-pointer"
          />
        </div>
        <div className="embla-thumbs mt-4 w-full">
          <div className="embla-thumbs__viewport" ref={emblaThumbsRef}>
            <div className="embla-thumbs__container flex flex-row gap-3">
              {candidates.map((_, index) => (
                <div
                  className={cn(
                    'embla-thumbs__slide grow overflow-visible',
                    index === selectedIndex
                      ? 'embla-thumbs__slide--selected'
                      : '',
                  )}
                >
                  <button
                    onClick={() => onThumbClick(index)}
                    className={cn(
                      'embla-thumbs__slide__number px-4 py-2 rounded-lg w-full hover:cursor-pointer bg-background transition-shadow',
                      index === selectedIndex
                        ? genderRingClassNames
                        : 'ring-border',
                      index === selectedIndex ? 'ring-[2px]' : 'ring-[1px]',
                    )}
                  >
                    <TextBody>{index + 1}</TextBody>
                  </button>
                </div>
              ))}
            </div>
          </div>
        </div>
        <div className="[&>*]:text-center mt-4 flex flex-col gap-[0px] w-fit mx-auto">
          <TextSub>⬅️Use 2 fingers on the trackpad to drag➡️</TextSub>
          <TextSub>Or click on the candidate cards</TextSub>
        </div>
      </div>
    </div>
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
    <Scoring.TabsFacade.Body className="flex flex-col gap-12">
      <div className="flex flex-col gap-4 items-center">
        <div className="[&>*]:text-center flex flex-col gap-1">
          <TextHeading>{ongoingSegment.name}</TextHeading>
          <TextBody>Score each candidate in the segment</TextBody>
        </div>
        <div className="flex flex-col items-center">
          <TextBody>🔒Scores are saved automatically</TextBody>
          <TextBody>🚫Scoring will close when a segment finishes</TextBody>
          <TextBody>
            👨🏻‍💻Contact organizers when there is a technical problem
          </TextBody>
        </div>
      </div>
      <div className="grid grid-rows-2 gap-4 select-none">
        <CandidateScoringCards
          gender={candidateGender.enum.FEMALE}
          candidates={femaleCandidates}
          scoresMap={candidateScoresMap}
        />
        <CandidateScoringCards
          gender={candidateGender.enum.MALE}
          candidates={maleCandidates}
          scoresMap={candidateScoresMap}
        />
      </div>
    </Scoring.TabsFacade.Body>
  ) : (
    <ScoringTabsFacadeBodyNoActiveSegmentFallback phase={ongoingPhase} />
  )

  return (
    <Scoring>
      <Scoring.Header>
        <Scoring.Header.Display className="text-center">
          {assignedPageant.title}
        </Scoring.Header.Display>
        <Scoring.Header.Title>{ongoingPhase.name}</Scoring.Header.Title>
        <Scoring.Header.Sub>
          Welcome, {capitalizeWords(judge.honorific) + '.'}{' '}
          {capitalizeWords(judge.firstName)} {capitalizeWords(judge.lastName)}!
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
