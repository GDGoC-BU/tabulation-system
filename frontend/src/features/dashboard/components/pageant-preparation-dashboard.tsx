import { useQueryClient } from '@tanstack/react-query'
import { TextBody } from '@/components/text'
import { useSelectedPageant } from '@/features/pageants/hooks/use-selected-pageant'
import useStatusChangeMutate from '@/features/state-machine/hooks/use-status-change-mutate'
import ConfirmDialog from '@/components/confirm-dialog'

export default function PageantPreparationDashboard() {
  const queryClient = useQueryClient()
  const { mutateAsync } = useStatusChangeMutate()
  const { data: selectedPageant, isLoading } = useSelectedPageant()
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
      <div className="flex flex-col items-center gap-4">
        <TextBody>
          Summary of pageant details will be show here. When pageant starts no
          modification will be allowed.
        </TextBody>
        <ConfirmDialog
          triggerLabel="Start Pageant"
          title="Start Pageant"
          description="This action cannot be undone. Are you sure you want to move to the next stage?"
          onConfirm={onClick}
        />
      </div>
    </div>
  )
}
