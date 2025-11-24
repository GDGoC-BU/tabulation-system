import { useQuery } from '@tanstack/react-query'
import { useSelectedPageantIdStore } from '../store/use-selected-pageant-id-store'
import pageantQueryOptions from '../query-options/pageant-query-options'

export function useSelectedPageant() {
  const { selectedPageantId: id } = useSelectedPageantIdStore((state) => state)
  return useQuery(pageantQueryOptions(id, { enabled: !!id }))
}
