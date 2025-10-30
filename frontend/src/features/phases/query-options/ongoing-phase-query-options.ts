import { queryOptions } from '@tanstack/react-query'
import { phaseDetailedSchema } from '../schemas'
import type { PhaseDetailed } from '../schemas'
import type { UseQueryOptions } from '@tanstack/react-query'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function ongoingPhaseQueryOptions<
  TData = PhaseDetailed,
  TError = string,
>(
  options?: Omit<
    UseQueryOptions<PhaseDetailed, TError, TData>,
    'queryKey' | 'queryFn'
  >,
) {
  return queryOptions({
    ...options,
    queryKey: ['phases', 'ongoing'],
    queryFn: async () => {
      try {
        const response = await api.get('/phases/ongoing')
        const parsedResponse = phaseDetailedSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error("/phases/ongoing response doesn't match schema!")
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
  })
}
