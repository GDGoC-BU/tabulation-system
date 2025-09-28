import { createFileRoute } from '@tanstack/react-router'
import type { PageantStatusValue } from '@/schemas'
import { useSelectedPageantQuery } from '@/features/pageants/hooks/use-selected-pageant-query'
import Console from '@/components/console'
import { TextBody } from '@/components/text'
import PageantPreparationDashboard from '@/features/dashboard/components/pageant-preparation-dashboard'
import PageantOngoingDashboard from '@/features/dashboard/components/pageant-ongoing-dashboard'
import PageantFinalizingDashboard from '@/features/dashboard/components/pageant-finalizing-dashboard'
import PageantClosedDashboard from '@/features/dashboard/components/pageant-closed-dashboard'

const pageantStatusComponentMap: Record<PageantStatusValue, React.FC> = {
  PREPARATION: PageantPreparationDashboard,
  ONGOING: PageantOngoingDashboard,
  FINALIZING: PageantFinalizingDashboard,
  CLOSED: PageantClosedDashboard,
}

export const Route = createFileRoute('/admin/console/dashboard')({
  component: AdminDashboard,
})

function AdminDashboard() {
  const { data: selectedPageant, isLoading } = useSelectedPageantQuery()
  if (isLoading || !selectedPageant) {
    return <TextBody>Loading dashboard...</TextBody>
  }

  const CurrentStatusComponent =
    pageantStatusComponentMap[selectedPageant.status.value]

  return (
    <Console>
      <Console.Header className="flex flex-row gap-4 items-center">
        <Console.Header.Title>
          Dashboard: {selectedPageant.title}
        </Console.Header.Title>
        <div
          className="py-2 px-4 rounded-md"
          style={{ backgroundColor: selectedPageant.status.color }}
        >
          <TextBody>{selectedPageant.status.value}</TextBody>
        </div>
      </Console.Header>
      <Console.Content className="w-full grow flex">
        <CurrentStatusComponent />
      </Console.Content>
    </Console>
  )
}
