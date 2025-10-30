import { queryOptions } from '@tanstack/react-query'
import { pageantHierarchySchema } from '../schemas'
import type { PageantHierarchy } from '../schemas'
import type { UseQueryOptions } from '@tanstack/react-query'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function pageantHierarchyQueryOptions<
  TData = PageantHierarchy,
  TError = string,
>(
  id: string | undefined | null,
  options?: Omit<
    UseQueryOptions<PageantHierarchy, TError, TData>,
    'queryKey' | 'queryFn'
  >,
) {
  return queryOptions({
    staleTime: 1000 * 60 * 10,
    enabled: !!id,
    ...options,
    queryKey: ['pageants', id, 'hierarchy'],
    queryFn: async () => {
      try {
        const response = await api.get(`/pageants/${id}/hierarchy`)
        const parsedResponse = pageantHierarchySchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error(
            `/pageant/id/hierarchy response doesn't match schema!`,
          )
        }
        return parsedResponse.data
      } catch (error) {
        console.log(error)
        throw errorResolver(error)
      }
    },
  })
}
