import { queryOptions } from '@tanstack/react-query'
import z from 'zod'
import { phaseSummarySchema } from '../schemas'
import type { PhaseSummary } from '../schemas'
import type { UseQueryOptions } from '@tanstack/react-query'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function phasesQueryOptions<
  TData = Array<PhaseSummary>,
  TError = string,
>(
  options?: Omit<
    UseQueryOptions<Array<PhaseSummary>, TError, TData>,
    'queryKey' | 'queryFn'
  >,
) {
  return queryOptions({
    staleTime: 1000 * 60 * 10,
    ...options,
    queryKey: ['phases'],
    queryFn: async () => {
      try {
        const response = await api.get('/phases')
        const parsedResponse = z
          .array(phaseSummarySchema)
          .safeParse(response.data)
        if (!parsedResponse.success) {
          console.error("/phases response doesn't match schema!")
          return []
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
  })
}
