import { queryOptions } from '@tanstack/react-query'
import { segmentDetailedSchema } from '../schemas'
import type { SegmentDetailed } from '../schemas'
import type { UseQueryOptions } from '@tanstack/react-query'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function ongoingSegmentQueryOptions<
  TData = SegmentDetailed | null,
  TError = string,
>(
  options?: Omit<
    UseQueryOptions<SegmentDetailed | null, TError, TData>,
    'queryKey' | 'queryFn'
  >,
) {
  return queryOptions({
    ...options,
    queryKey: ['segments', 'ongoing'],
    queryFn: async () => {
      try {
        const response = await api.get('/segments/ongoing')

        if (response.status === 204) {
          return null
        }

        const parsedResponse = segmentDetailedSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error("/segments/ongoing response doesn't match schema!")
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
  })
}
