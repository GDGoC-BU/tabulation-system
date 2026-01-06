import { queryOptions } from '@tanstack/react-query'
import { awardDetailedSchema } from '../schemas'
import type { AwardDetailed } from '../schemas'
import type { UseQueryOptions } from '@tanstack/react-query'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function awardQueryOptions<
  TData = AwardDetailed,
  TError = string,
>(
  id: string | undefined | null,
  options?: Omit<
    UseQueryOptions<AwardDetailed, TError, TData>,
    'queryKey' | 'queryFn'
  >,
) {
  return queryOptions({
    staleTime: 1000 * 60 * 10,
    enabled: !!id,
    ...options,
    queryKey: ['awards', id],
    queryFn: async () => {
      try {
        const response = await api.get(`/awards/${id}`)
        const parsedResponse = awardDetailedSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error(`/awards/${id} response doesn't match schema!`)
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
  })
}
