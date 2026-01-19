import { useQuery } from '@tanstack/react-query'
import { useMemo } from 'react'
import type { SegmentHierarchy } from '@/features/segments/schemas'
import type { PhaseHierarchy } from '@/features/phases/schemas'
import { useSelectedPageant } from '@/features/pageants/hooks/use-selected-pageant'
import pageantHierarchyQueryOptions from '@/features/pageants/query-options/pageant-hierarchy-query-options'
import { TextBody, TextSub } from '@/components/text'
import { phaseSegmentStatusValue } from '@/schemas'
import ConfirmDialog from '@/components/confirm-dialog'

function Badge({ children }: { children: React.ReactNode }) {
  return (
    <div className="border size-fit px-2 py-1 rounded-full flex flex-row gap-1 items-center">
      <TextSub className="text-foreground">{children}</TextSub>
    </div>
  )
}

function SegmentCard({
  segment,
}: {
  segment: SegmentHierarchy
  isDisabled: boolean
}) {
  return (
    <div className="grow p-6 border rounded-lg flex flex-col items-center gap-2">
      <div className="flex flex-col gap-1 items-center">
        <TextBody>{segment.name}</TextBody>
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

function determinNextAction(phases: Array<PhaseHierarchy>) {
  return phases
}

export default function PageantOngoinDashboard() {
  const { data: selectedPageant, isLoading: isSelectedPageantLoading } =
    useSelectedPageant()
  const { data: pageantHierarchy, isLoading: isPageantHierarchyLoading } =
    useQuery(
      pageantHierarchyQueryOptions(selectedPageant!.id, {
        enabled: !!selectedPageant,
      }),
    )

  const PENDING = phaseSegmentStatusValue.enum.PENDING
  const ONGOING = phaseSegmentStatusValue.enum.ONGOING
  const CLOSED = phaseSegmentStatusValue.enum.CLOSED

  const startButtonLocation = useMemo(() => {
    if (!pageantHierarchy) return null
    return determinNextAction(pageantHierarchy.phases)
  }, [pageantHierarchy])

  if (isSelectedPageantLoading || isPageantHierarchyLoading) {
    return (
      <div className="w-full grid place-items-center">
        <TextBody>Loading...</TextBody>
      </div>
    )
  }

  return (
    <div className="w-full">
      <div className="flex flex-col gap-4 w-full items-center">
        {pageantHierarchy?.phases
          .sort((a, b) => a.sequence - b.sequence)
          .map((phase) => {
            return (
              <PhaseCard phase={phase}>
                {phase.segments
                  .sort((a, b) => a.sequence - b.sequence)
                  .map((segment) => {
                    const isDisabled = segment.status === PENDING

                    return (
                      <SegmentCard segment={segment} isDisabled={isDisabled} />
                    )
                  })}
              </PhaseCard>
            )
          })}
      </div>
      <div className="mt-4">
        <ConfirmDialog
          triggerLabel={'Finalize Pageant'}
          title={'Finalize Pageant'}
          description="This action cannot be undone. Are you sure you want to move to the next stage?"
          onConfirm={() => {}}
        />
      </div>
    </div>
  )
}
