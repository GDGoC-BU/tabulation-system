import { useSelectedPageantId } from '../store/use-selected-pageant-id'
import { usePageantQuery } from './use-pageant-query'

export function useSelectedPageantQuery() {
  const { selectedPageantId: id } = useSelectedPageantId((state) => state)
  return usePageantQuery(id ?? '')
}
