import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useMemo } from 'react'
import { Star } from 'lucide-react'
import type { SegmentHierarchy } from '@/features/segments/schemas'
import type { PhaseHierarchy } from '@/features/phases/schemas'
import { useSelectedPageant } from '@/features/pageants/hooks/use-selected-pageant'
import pageantHierarchyQueryOptions from '@/features/pageants/query-options/pageant-hierarchy-query-options'
import { TextBody, TextSub } from '@/components/text'
import { phaseSegmentStatusValue } from '@/schemas'
import ConfirmDialog from '@/components/confirm-dialog'
import api from '@/lib/axios'

type NextAction = {
  endpoint: string
  label: string
} | null

function determinNextAction(phases: Array<PhaseHierarchy>): NextAction {
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

export default function PageantOngoinDashboard() {
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
    return determinNextAction(pageantHierarchy.phases)
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
    <div className="w-full">
      <div className="flex flex-col gap-4 w-full items-center">
        {pageantHierarchy.phases
          .sort((a, b) => a.sequence - b.sequence)
          .map((phase) => {
            return (
              <PhaseCard phase={phase}>
                {phase.segments
                  .sort((a, b) => a.sequence - b.sequence)
                  .map((segment) => {
                    return <SegmentCard segment={segment} />
                  })}
              </PhaseCard>
            )
          })}
      </div>
      <div className="mt-4">
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
      </div>
    </div>
  )
}
