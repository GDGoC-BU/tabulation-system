import { queryOptions } from '@tanstack/react-query'
import { pageantsSchema } from '../schemas'
import type { PageantSummary } from '../schemas'
import type { UseQueryOptions } from '@tanstack/react-query'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function pageantsQueryOptions<
  TData = Array<PageantSummary>,
  TError = string,
>(
  options?: Omit<
    UseQueryOptions<Array<PageantSummary>, TError, TData>,
    'queryKey' | 'queryFn'
  >,
) {
  return queryOptions({
    staleTime: 1000 * 60 * 10,
    ...options,
    queryKey: ['pageants'],
    queryFn: async () => {
      try {
        const response = await api.get('/pageants')
        const parsedResponse = pageantsSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          console.error("/pageants response doesn't match schema!")
          return []
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
  })
}
