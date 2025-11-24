import { queryOptions } from '@tanstack/react-query'
import { pageantSummarySchema } from '../schemas'
import type { PageantSummary } from '../schemas'
import type { UseQueryOptions } from '@tanstack/react-query'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function pageantQueryOptions<
  TData = PageantSummary,
  TError = string,
>(
  id: string | undefined | null,
  options?: Omit<
    UseQueryOptions<PageantSummary, TError, TData>,
    'queryKey' | 'queryFn'
  >,
) {
  return queryOptions({
    staleTime: 1000 * 60 * 10,
    enabled: !!id,
    ...options,
    queryKey: ['pageants', id],
    queryFn: async () => {
      try {
        const response = await api.get(`/pageants/${id}`)
        const parsedResponse = pageantSummarySchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error(`/pageant/id response doesn't match schema!`)
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
  })
}
