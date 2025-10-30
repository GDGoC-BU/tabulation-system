import { queryOptions } from '@tanstack/react-query'
import { judgeSummarySchema } from '../schemas'
import type { JudgeSummary } from '../schemas'
import type { UseQueryOptions } from '@tanstack/react-query'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function judgeQueryOptions<
  TData = JudgeSummary,
  TError = string,
>(
  id: string | undefined | null,
  options?: Omit<
    UseQueryOptions<JudgeSummary, TError, TData>,
    'queryKey' | 'queryFn'
  >,
) {
  return queryOptions({
    staleTime: 1000 * 60 * 10,
    enabled: !!id,
    ...options,
    queryKey: ['judges', id],
    queryFn: async () => {
      try {
        const response = await api.get(`/judges/${id}`)
        const parsedResponse = judgeSummarySchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error(`/judges/id response doesn't match schema!`)
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
  })
}
