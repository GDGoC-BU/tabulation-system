import { useMemo } from 'react'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import type { Phases } from '@/features/phases/schemas'
import type { Segments } from '@/features/segments/schemas'
import { TextBody } from '@/components/text'
import determineNextPhaseSegmentStatusAction from '@/features/state-machine/lib/determine-next-phase-segment-status-action'
import { useSelectedPageant } from '@/features/pageants/hooks/use-selected-pageant'
import useStatusChangeMutate from '@/features/state-machine/hooks/use-status-change-mutate'
import ConfirmDialog from '@/components/confirm-dialog'
import phasesQueryOptions from '@/features/phases/query-options/phases-query-options'
import segmentsQueryOptions from '@/features/segments/query-options/segments-query-options'

function PhaseSegmentTable({
  phases,
  segments,
}: {
  phases: Phases
  segments: Segments
}) {
  return (
    <div>
      {phases.map((phase) => {
        return (
          <div key={phase.id} className="pb-8 flex flex-col gap-4">
            <div key={phase.id} className="flex flex-row gap-4 justify-between">
              <div className="flex flex-row gap-2">
                <TextBody>{phase.sequence}</TextBody>
                <TextBody>{phase.name}</TextBody>
              </div>
              <TextBody>{phase.status}</TextBody>
            </div>
            {segments.map((segment) => {
              if (segment.phase.id === phase.id) {
                return (
                  <div
                    key={segment.id}
                    className="ml-8 flex flex-row gap-4 justify-between"
                  >
                    <div className="flex flex-row gap-2">
                      <TextBody>
                        {phase.sequence}.{segment.sequence}
                      </TextBody>
                      <TextBody>{segment.name}</TextBody>
                    </div>
                    <TextBody>{segment.status}</TextBody>
                  </div>
                )
              }
            })}
          </div>
        )
      })}
    </div>
  )
}

export default function PageantOngoingDashboard() {
  const { data: selectedPageant } = useSelectedPageant()
  const { data: phases } = useQuery(phasesQueryOptions())
  const { data: segments } = useQuery(segmentsQueryOptions())
  const { mutateAsync: changeState } = useStatusChangeMutate()
  const queryClient = useQueryClient()

  /* Sort phases and segments just incase */
  const sortedPhases = useMemo(() => {
    if (!phases) return []
    return [...phases].sort((a, b) => a.sequence - b.sequence)
  }, [phases])
  const sortedSegments = useMemo(() => {
    if (!segments) return []
    return [...segments].sort((a, b) => a.sequence - b.sequence)
  }, [segments])

  const nextAction = useMemo(() => {
    if (!sortedPhases.length || !sortedSegments.length) return null
    return determineNextPhaseSegmentStatusAction(sortedPhases, sortedSegments)
  }, [sortedPhases, sortedSegments, selectedPageant])

  async function onClick() {
    /* If all phases and segments are closed, finalize the pageant */
    if (!nextAction) {
      await changeState(`/pageants/${selectedPageant?.id}/finalize`)
      queryClient.invalidateQueries({ queryKey: ['pageants'] })
      return
    }

    await changeState(nextAction.endpoint)
    queryClient.invalidateQueries({ queryKey: ['phases'] })
    queryClient.invalidateQueries({ queryKey: ['segments'] })
    console.log(nextAction.endpoint)
  }

  return (
    <div className="flex flex-row gap-4">
      <div className="border rounded-lg w-fit h-fit">
        <div className="px-4 py-4 border-b">
          <TextBody>Phases and Segments</TextBody>
        </div>
        <div className="px-4 pt-4 ">
          <PhaseSegmentTable phases={sortedPhases} segments={sortedSegments} />
        </div>
      </div>
      {/* <Button onClick={onClick}>
        {nextAction ? nextAction.label : 'Finalize Pageant'}
      </Button> */}
      <ConfirmDialog
        triggerLabel={nextAction ? nextAction.label : 'Finalize Pageant'}
        title={nextAction ? nextAction.label : 'Finalize Pageant'}
        description="This action cannot be undone. Are you sure you want to move to the next stage?"
        onConfirm={onClick}
      />
    </div>
  )
}
