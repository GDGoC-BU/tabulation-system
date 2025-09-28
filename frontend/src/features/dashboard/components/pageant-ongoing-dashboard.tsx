import { TextBody } from '@/components/text'
import { usePhasesQuery } from '@/features/phases/hooks/use-phases-query'
import { useSegmentsQuery } from '@/features/segments/hooks/use-phases-query'

export default function PageantOngoingDashboard() {
  const { data: phases } = usePhasesQuery()
  const { data: segments } = useSegmentsQuery()

  return (
    <div>
      <div className="border rounded-lg w-fit ">
        <div className="px-4 py-4 border-b">
          <TextBody>Phases and Segments</TextBody>
        </div>
        <div className="px-4 pt-4">
          {phases?.map((phase) => {
            return (
              <div key={phase.id} className="pb-8 flex flex-col gap-4">
                <div
                  key={phase.id}
                  className="flex flex-row gap-4 justify-between"
                >
                  <div className="flex flex-row gap-2">
                    <TextBody>{phase.sequence}</TextBody>
                    <TextBody>{phase.name}</TextBody>
                  </div>
                  <TextBody>{phase.status}</TextBody>
                </div>
                {segments?.map((segment) => {
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
      </div>
    </div>
  )
}
