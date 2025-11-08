import { queryOptions } from '@tanstack/react-query'
import z from 'zod'
import { segmentSummarySchema } from '../schemas'
import type { SegmentSummary } from '../schemas'
import type { UseQueryOptions } from '@tanstack/react-query'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function segmentsQueryOptions<
  TData = Array<SegmentSummary> | null,
  TError = string,
>(
  options?: Omit<
    UseQueryOptions<Array<SegmentSummary> | null, TError, TData>,
    'queryKey' | 'queryFn'
  >,
) {
  return queryOptions({
    staleTime: 1000 * 60 * 10,
    ...options,
    queryKey: ['segments'],
    queryFn: async () => {
      try {
        const response = await api.get('/segments')
        const parsedResponse = z
          .array(segmentSummarySchema)
          .safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error("/segments response doesn't match schema!")
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
  })
}
