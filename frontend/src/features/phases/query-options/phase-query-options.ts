import { queryOptions } from '@tanstack/react-query'
import { phaseDetailedSchema } from '../schemas'
import type { PhaseDetailed } from '../schemas'
import type { UseQueryOptions } from '@tanstack/react-query'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function phaseQueryOptions<
  TData = PhaseDetailed,
  TError = string,
>(
  id: string | undefined | null,
  options?: Omit<
    UseQueryOptions<PhaseDetailed, TError, TData>,
    'queryKey' | 'queryFn'
  >,
) {
  return queryOptions({
    staleTime: 1000 * 60 * 10,
    enabled: !!id,
    ...options,
    queryKey: ['phases', id],
    queryFn: async () => {
      try {
        const response = await api.get(`/phases/${id}`)
        const parsedResponse = phaseDetailedSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error(`/phases/${id} response doesn't match schema!`)
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
  })
}
