import { queryOptions } from '@tanstack/react-query'
import { scoresSchema } from '../schemas'
import type { Scores } from '../schemas'
import type { UseQueryOptions } from '@tanstack/react-query'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function scoresQueryOptions<TData = Scores, TError = string>(
  params: {
    judgeId?: string
    candidateId?: string
    criterionId?: string
    segmentId?: string
  },
  options?: Omit<
    UseQueryOptions<Scores, TError, TData>,
    'queryKey' | 'queryFn'
  >,
) {
  return queryOptions({
    staleTime: 1000 * 60 * 10,
    ...options,
    queryKey: [
      'scores',
      /* Queries are matched exatcly as to the values of the params.
         When invalidating, ensure that all other fields are set */
      {
        judgeId: params.judgeId ?? null,
        candidateId: params.candidateId ?? null,
        criterionId: params.criterionId ?? null,
        segmentId: params.segmentId ?? null,
      },
    ],
    queryFn: async () => {
      try {
        const response = await api.get('/scores', { params: params })
        const parsedResponse = scoresSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          console.error("/scores response doesn't match schema!")
          return []
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
  })
}
