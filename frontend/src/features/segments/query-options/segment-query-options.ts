import { queryOptions } from '@tanstack/react-query'
import { segmentDetailedSchema } from '../schemas'
import type { SegmentDetailed } from '../schemas'
import type { UseQueryOptions } from '@tanstack/react-query'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function segmentQueryOptions<
  TData = SegmentDetailed | null,
  TError = string,
>(
  id: string | undefined | null,
  options?: Omit<
    UseQueryOptions<SegmentDetailed | null, TError, TData>,
    'queryKey' | 'queryFn'
  >,
) {
  return queryOptions({
    enabled: !!id,
    staleTime: 1000 * 60 * 10,
    ...options,
    queryKey: ['segments', id],
    queryFn: async () => {
      try {
        const response = await api.get(`/segments/${id}`)
        const parsedResponse = segmentDetailedSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error(`/segments/${id} response doesn't match schema!`)
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
  })
}
