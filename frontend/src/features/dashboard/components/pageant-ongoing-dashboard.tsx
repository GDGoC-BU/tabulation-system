import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useMemo } from 'react'
import { Star } from 'lucide-react'
import type { SegmentHierarchy } from '@/features/segments/schemas'
import type { PhaseHierarchy } from '@/features/phases/schemas'
import type { PageantHierarchy } from '@/features/pageants/schemas'
import { useSelectedPageant } from '@/features/pageants/hooks/use-selected-pageant'
import pageantHierarchyQueryOptions from '@/features/pageants/query-options/pageant-hierarchy-query-options'
import {
  TextBody,
  TextHeading,
  TextSub,
  textBodyClassName,
  textHeadingClassName,
} from '@/components/text'
import { phaseSegmentStatusValue } from '@/schemas'
import ConfirmDialog from '@/components/confirm-dialog'
import api from '@/lib/axios'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import segmentQualificationLeaderboardQueryOptions from '@/features/segments/query-options/segment-qualification-leaderboard-query-options'
import { groupLeaderboardEntriesByGender } from '@/features/leaderboard/lib/group-leaderboard-entries-by-gender'
import Table from '@/components/table'
import { leadboardTableColumns } from '@/features/leaderboard/components/leaderboard-table-columns'
import FormulaRenderer from '@/features/formula/deprecated-components/formula-renderer'
import useFormulaCriterionLookup from '@/features/formula/hooks/use-formula-criterion-lookup'

type NextAction = {
  endpoint: string
  label: string
  segment?: SegmentHierarchy
} | null

function determineNextAction(phases: Array<PhaseHierarchy>): NextAction {
  const PENDING = phaseSegmentStatusValue.enum.PENDING
  const ONGOING = phaseSegmentStatusValue.enum.ONGOING
  const CLOSED = phaseSegmentStatusValue.enum.CLOSED

  /* Get the first Phase that is not CLOSED */
  const phase = [...phases]
    .sort((a, b) => a.sequence - b.sequence)
    .find((p) => p.status !== CLOSED)

  /* If all are CLOSED, we are all done! */
  if (!phase) {
    return null
  }

  /* If Phase is still PENDING, start it */
  if (phase.status === PENDING) {
    return {
      endpoint: `/phases/${phase.id}/start`,
      label: `Start ${phase.name}`,
    }
  }

  /* If Phase is ONGOING, handle its Segments */
  if (phase.status === ONGOING) {
    /* Get the Segments for the Phase */
    const segmentsOnTheOngoingPhase = [...phase.segments].sort(
      (a, b) => a.sequence - b.sequence,
    )

    /* Find the first non-CLOSED segment */
    const segment = segmentsOnTheOngoingPhase.find((s) => s.status !== CLOSED)

    /* If all segments are CLOSED, close the Phase */
    if (!segment) {
      return {
        endpoint: `/phases/${phase.id}/close`,
        label: `Close ${phase.name}`,
      }
    }

    /* If Segment is PENDING, start it */
    if (segment.status === PENDING) {
      return {
        endpoint: `/segments/${segment.id}/start`,
        label: `Start ${segment.name}`,
        segment: segment,
      }
    }

    /* If Segment is ONGOING, close it */
    if (segment.status === ONGOING) {
      return {
        endpoint: `/segments/${segment.id}/close`,
        label: `Close ${segment.name}`,
      }
    }
  }

  return null
}

function Badge({ children }: { children: React.ReactNode }) {
  return (
    <div className="border size-fit px-2 py-1 rounded-full flex flex-row gap-1 items-center">
      <TextSub className="text-foreground">{children}</TextSub>
    </div>
  )
}

function SegmentCard({ segment }: { segment: SegmentHierarchy }) {
  return (
    <div className="grow p-6 border rounded-lg flex flex-col items-center gap-2">
      <div className="flex flex-col gap-1 items-center">
        <div className="flex flex-row gap-1 items-center">
          {segment.qualificationLeaderboard !== null && <Star width={18} />}
          <TextBody>{segment.name}</TextBody>
        </div>
        <Badge>{segment.status}</Badge>
      </div>
      <div className="flex flex-col gap-2 hover:cursor-pointer">
        <TextSub>View Leaderboard</TextSub>
      </div>
    </div>
  )
}

function PhaseCard({
  phase,
  children,
}: {
  phase: PhaseHierarchy
  children: React.ReactNode
}) {
  return (
    <div className="w-full p-4 border rounded-lg flex flex-col gap-4">
      <div className="flex flex-row gap-2 items-center">
        <TextBody>{`${phase.sequence} ) ${phase.name}`}</TextBody>
        <Badge>{phase.status}</Badge>
      </div>
      <div className="flex flex-row gap-4">{children}</div>
    </div>
  )
}

function CalculateSegmentQualificationButton({
  children,
  segmentId,
  pageantId,
  ...props
}: {
  children: React.ReactNode
  segmentId: string
  pageantId: string
}) {
  const queryClient = useQueryClient()
  const { mutateAsync } = useMutation({
    mutationFn: async (url: string | null) => {
      if (!url) return
      await api.post(url)
    },
  })

  return (
    <Button
      {...props}
      onClick={async () => {
        await mutateAsync(
          `segments/${segmentId}/qualificationLeaderboard/calculate`,
        )
        queryClient.invalidateQueries({
          queryKey: ['pageants', pageantId, 'hierarchy'],
        })
        queryClient.invalidateQueries({
          queryKey: ['segments', segmentId, 'qualificationLeaderboard'],
        })
      }}
    >
      {children}
    </Button>
  )
}

function SegmentQualificationResultsViewer({
  segment,
}: {
  segment: SegmentHierarchy
}) {
  const {
    data: qualificationLeaderboard,
    isFetching: isQualificationLeaderboardFetching,
  } = useQuery(
    segmentQualificationLeaderboardQueryOptions(segment.id, {
      enabled:
        segment.qualificationLeaderboard !== null &&
        segment.qualificationLeaderboard.lastCalculatedAt !== null,
    }),
  )

  const { maleCandidates, femaleCandidates } = useMemo(() => {
    if (!qualificationLeaderboard) {
      return { maleCandidates: [], femaleCandidates: [] }
    }
    const candidates = groupLeaderboardEntriesByGender(
      qualificationLeaderboard.entries,
    )
    return {
      maleCandidates: candidates.maleCandidates.sort(
        (a, b) => b.score - a.score,
      ),
      femaleCandidates: candidates.femaleCandidates.sort(
        (a, b) => b.score - a.score,
      ),
    }
  }, [qualificationLeaderboard])

  if (isQualificationLeaderboardFetching) {
    return (
      <div className="w-full h-full grid place-items-center">
        <TextBody>Calculating...</TextBody>
      </div>
    )
  }

  if (!qualificationLeaderboard) {
    return (
      <div className="w-full grid place-items-center">
        <TextBody>{segment.name} qualifiers has not been calculated </TextBody>
      </div>
    )
  }

  return (
    <div className="w-full overflow-y-scroll mt-2">
      <div className="overflow-y-hidden flex flex-col gap-8">
        <div className="flex flex-col gap-4">
          <div className="">
            <TextHeading>Female Candidates</TextHeading>
          </div>
          <div className="">
            <Table.LeaderboardNew
              columns={leadboardTableColumns}
              data={femaleCandidates}
              formula={qualificationLeaderboard.formula}
            />
          </div>
        </div>
        <div className="flex flex-col gap-4">
          <div className="">
            <TextHeading>Male Candidates</TextHeading>
          </div>
          <div className="">
            <Table.LeaderboardNew
              columns={leadboardTableColumns}
              data={maleCandidates}
              formula={qualificationLeaderboard.formula}
            />
          </div>
        </div>
      </div>
    </div>
  )
}

function SegmentQualificationCalculator({
  segment,
  pageantId,
}: {
  segment: SegmentHierarchy
  pageantId: string
}) {
  return (
    <div className="grow grid place-items-center">
      <CalculateSegmentQualificationButton
        segmentId={segment.id}
        pageantId={pageantId}
      >
        Calculate qualifiers
      </CalculateSegmentQualificationButton>
    </div>
  )
}

function SegmentQualificationVerification({
  segment,
  pageant,
}: {
  segment: SegmentHierarchy
  pageant: PageantHierarchy
}) {
  const queryClient = useQueryClient()
  const { mutateAsync } = useMutation({
    mutationFn: async (url: string | null) => {
      if (!url) return
      await api.post(url)
    },
  })

  const criterionLookup = useFormulaCriterionLookup(pageant.phases)

  if (segment.qualificationLeaderboard === null) {
    return <TextBody>Segment has no qualifications</TextBody>
  }

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button>View {segment.name} qualifiers</Button>
      </DialogTrigger>
      <DialogContent className="w-[calc(100vw-2rem)] h-[calc(100vh-2rem)] !max-w-none flex flex-col gap-6">
        <div className="space-y-2">
          <DialogTitle className={textHeadingClassName}>
            {segment.name} Qualification Results
          </DialogTitle>
          <DialogDescription className={textBodyClassName}>
            View the qualified candidates for the segment.
          </DialogDescription>
          <TextBody>
            Candidate limit: {segment.qualificationLeaderboard.selectionCount}
          </TextBody>
          <div className="flex flex-row gap-2">
            <TextBody>Formula: </TextBody>
            <FormulaRenderer
              formula={segment.qualificationLeaderboard.formula.text}
              criterionLookup={criterionLookup}
            />
          </div>
        </div>
        {segment.qualificationLeaderboard.lastCalculatedAt !== null ? (
          <SegmentQualificationResultsViewer segment={segment} />
        ) : (
          <SegmentQualificationCalculator
            segment={segment}
            pageantId={pageant.id}
          />
        )}
        {segment.qualificationLeaderboard.lastCalculatedAt !== null && (
          <div className="flex flex-row gap-4">
            <CalculateSegmentQualificationButton
              // @ts-ignore Can make this type-safe later on. Or find another abstraction
              variant="outline"
              segmentId={segment.id}
              pageantId={pageant.id}
            >
              Recalculate
            </CalculateSegmentQualificationButton>
            <Button
              onClick={async () => {
                await mutateAsync(`segments/${segment.id}/start`)
                queryClient.invalidateQueries({
                  queryKey: ['pageants', pageant.id, 'hierarchy'],
                })
              }}
            >
              Confirm and Start {segment.name}
            </Button>
          </div>
        )}
      </DialogContent>
    </Dialog>
  )
}

export default function PageantOngoingDashboard() {
  const queryClient = useQueryClient()

  const { data: selectedPageant, isLoading: isSelectedPageantLoading } =
    useSelectedPageant()
  const { data: pageantHierarchy, isLoading: isPageantHierarchyLoading } =
    useQuery(
      pageantHierarchyQueryOptions(selectedPageant!.id, {
        enabled: !!selectedPageant,
      }),
    )

  const { mutateAsync } = useMutation({
    mutationFn: async (url: string | null) => {
      if (!url) return
      await api.post(url)
    },
  })

  const nextAction = useMemo(() => {
    if (!pageantHierarchy) return null
    return determineNextAction(pageantHierarchy.phases)
  }, [pageantHierarchy])

  if (
    isSelectedPageantLoading ||
    isPageantHierarchyLoading ||
    !pageantHierarchy
  ) {
    return (
      <div className="w-full grid place-items-center">
        <TextBody>Loading...</TextBody>
      </div>
    )
  }

  return (
    <div className="w-full flex flex-col gap-4">
      <div className="flex flex-col gap-4 w-full items-center">
        {pageantHierarchy.phases
          .sort((a, b) => a.sequence - b.sequence)
          .map((phase) => {
            return (
              <PhaseCard phase={phase} key={phase.id}>
                {phase.segments
                  .sort((a, b) => a.sequence - b.sequence)
                  .map((segment) => {
                    return <SegmentCard segment={segment} key={segment.id} />
                  })}
              </PhaseCard>
            )
          })}
      </div>
      <div>
        {nextAction?.segment &&
        nextAction.segment.qualificationLeaderboard !== null ? (
          <SegmentQualificationVerification
            segment={nextAction.segment}
            pageant={pageantHierarchy}
          />
        ) : (
          <ConfirmDialog
            triggerLabel={nextAction ? nextAction.label : 'Finalize Pageant'}
            title={nextAction ? nextAction.label : 'Finalize Pageant'}
            description="This action cannot be undone. Are you sure you want to move to the next stage?"
            onConfirm={async () => {
              await mutateAsync(nextAction ? nextAction.endpoint : null)
              queryClient.invalidateQueries({
                queryKey: ['pageants', pageantHierarchy.id, 'hierarchy'],
              })
            }}
          />
        )}
      </div>
    </div>
  )
}
