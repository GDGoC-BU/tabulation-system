import { createFileRoute } from '@tanstack/react-router'
import { useQueryClient } from '@tanstack/react-query'
import type { PageantStatusValue } from '@/schemas'
import { useSelectedPageantQuery } from '@/features/pageants/hooks/use-selected-pageant-query'
import Console from '@/components/console'
import { Button } from '@/components/ui/button'
import { TextBody } from '@/components/text'
import usePageantStatusChangeMutate from '@/features/pageants/hooks/use-pageant-status-change-mutate'
import { usePhasesQuery } from '@/features/phases/hooks/use-phases-query'

type PageantStatusMeta = {
  label: string
  description: string
  nextAction?: {
    label: string
    endpoint: (id: string) => string
  }
}

const pageantStatusMeta: Record<PageantStatusValue, PageantStatusMeta> = {
  PREPARATION: {
    label: 'Preparation Stage',
    description: '',
    nextAction: {
      label: 'Start Pageant',
      endpoint: (id) => `/pageants/${id}/start`,
    },
  },
  ONGOING: {
    label: 'Ongoing',
    description: '',
    nextAction: {
      label: 'Finalize Pageant',
      endpoint: (id) => `/pageants/${id}/finalize`,
    },
  },
  FINALIZING: {
    label: 'Finalizing...',
    description: '',
    nextAction: {
      label: 'Close Pageant',
      endpoint: (id) => `/pageants/${id}/close`,
    },
  },
  CLOSED: {
    label: 'Closed',
    description: '',
  },
}

export const Route = createFileRoute('/admin/console/dashboard')({
  component: AdminDashboard,
})

function AdminDashboard() {
  const { data: selectedPageant } = useSelectedPageantQuery()
  const queryClient = useQueryClient()
  const { mutateAsync } = usePageantStatusChangeMutate()
  const { data: phases, isLoading: isPhasesLoading } = usePhasesQuery()

  if (!selectedPageant) {
    return (
      <Console>
        <Console.Header className="flex flex-row justify-between">
          <Console.Header.Title>Dashboard</Console.Header.Title>
        </Console.Header>
        <Console.Content>
          <TextBody>No pageant selected...</TextBody>
        </Console.Content>
      </Console>
    )
  }

  const pageantMeta = pageantStatusMeta[selectedPageant.status.value]

  async function onPageantStatusChange() {
    if (!pageantMeta.nextAction || !selectedPageant) return
    const pageantId = selectedPageant.id
    const pageantStatusChangeEndpoint =
      pageantMeta.nextAction.endpoint(pageantId)
    await mutateAsync(pageantStatusChangeEndpoint)
    queryClient.invalidateQueries({ queryKey: ['pageants'] })
    queryClient.invalidateQueries({ queryKey: ['pageants', pageantId] })
  }

  return (
    <Console>
      <Console.Header className="flex flex-row gap-4 items-center">
        <Console.Header.Title>
          {selectedPageant.title} Dashboard
        </Console.Header.Title>
        <div
          className="py-2 px-4 rounded-md"
          style={{ backgroundColor: selectedPageant.status.color }}
        >
          <TextBody>{selectedPageant.status.value}</TextBody>
        </div>
      </Console.Header>
      <Console.Content>
        {pageantMeta.nextAction && (
          <div className="">
            <Button onClick={onPageantStatusChange}>
              {pageantMeta.nextAction.label}
            </Button>
          </div>
        )}

        {/* PHASES */}
        <div className="border rounded-lg p-4 w-fit mt-8">
          <div>
            <TextBody>Phases</TextBody>
          </div>
          <div>
            {isPhasesLoading && <TextBody>Loading...</TextBody>}
            {phases?.map((phase) => {
              return (
                <div
                  key={phase.id}
                  className="flex flex-row gap-4 justify-between"
                >
                  <TextBody>{phase.name}</TextBody>
                  <TextBody>{phase.status}</TextBody>
                </div>
              )
            })}
          </div>
        </div>
      </Console.Content>
    </Console>
  )
}
