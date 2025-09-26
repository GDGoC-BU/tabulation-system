import { createFileRoute } from '@tanstack/react-router'
import { useQueryClient } from '@tanstack/react-query'
import type { PageantStatusValue } from '@/features/pageants/schemas'
import { useSelectedPageantQuery } from '@/features/pageants/hooks/use-selected-pageant-query'
import Console from '@/components/console'
import { Button } from '@/components/ui/button'
import { TextBody } from '@/components/text'
import usePageantStatusChangeMutate from '@/features/pageants/hooks/use-pageant-status-change-mutate'

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
  const { data } = useSelectedPageantQuery()
  const queryClient = useQueryClient()
  const { mutateAsync } = usePageantStatusChangeMutate()

  if (!data) {
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

  const pageantMeta = pageantStatusMeta[data.status.value]

  async function onPageantStatusChange() {
    if (!pageantMeta.nextAction || !data) return
    const pageantId = data.id
    const pageantStatusChangeEndpoint =
      pageantMeta.nextAction.endpoint(pageantId)
    await mutateAsync(pageantStatusChangeEndpoint)
    queryClient.invalidateQueries({ queryKey: ['pageants'] })
    queryClient.invalidateQueries({ queryKey: ['pageants', pageantId] })
  }

  return (
    <Console>
      <Console.Header className="flex flex-row gap-4 items-center">
        <Console.Header.Title>{data.title} Dashboard</Console.Header.Title>
        <div
          className="py-2 px-4 rounded-md"
          style={{ backgroundColor: data.status.color }}
        >
          <TextBody>{data.status.value}</TextBody>
        </div>
      </Console.Header>
      <Console.Content>
        {pageantMeta.nextAction && (
          <Button onClick={onPageantStatusChange}>
            {pageantMeta.nextAction.label}
          </Button>
        )}
      </Console.Content>
    </Console>
  )
}
