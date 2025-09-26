import { useSelectedPageantIdStore } from '../store/use-selected-pageant-id-store'
import { usePageantQuery } from './use-pageant-query'

export function useSelectedPageantQuery() {
  const { selectedPageantId: id } = useSelectedPageantIdStore((state) => state)
  return usePageantQuery(id ?? '')
}
