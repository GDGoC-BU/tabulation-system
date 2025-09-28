import { useQueryClient } from '@tanstack/react-query'
import { TextBody } from '@/components/text'
import { Button } from '@/components/ui/button'
import { useSelectedPageantQuery } from '@/features/pageants/hooks/use-selected-pageant-query'
import useStatusChangeMutate from '@/features/state-machine/hooks/use-status-change-mutate'

export default function PageantPreparationDashboard() {
  const queryClient = useQueryClient()
  const { mutateAsync } = useStatusChangeMutate()
  const { data: selectedPageant, isLoading } = useSelectedPageantQuery()
  if (isLoading || !selectedPageant) {
    return <TextBody>Loading Preperation Dashboard...</TextBody>
  }

  async function onClick() {
    await mutateAsync(`/pageants/${selectedPageant?.id}/start`)
    queryClient.invalidateQueries({ queryKey: ['pageants'] })
    queryClient.invalidateQueries({
      queryKey: ['pageants', selectedPageant?.id],
    })
  }

  return (
    <div className="grow grid place-items-center">
      <Button onClick={onClick}>Start Pageant</Button>
    </div>
  )
}
