import { queryOptions, useQuery } from '@tanstack/react-query'
import { scoresSchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useScoresQuery(
  params: {
    judgeId?: string
    candidateId?: string
    criterionId?: string
    segmentId?: string
  },
  enabled?: boolean,
) {
  return useQuery({
    queryKey: [
      'scores',
      params.judgeId,
      params.segmentId,
      params.candidateId,
      params.criterionId,
    ],
    queryFn: async () => {
      const parameters = {
        ...(params.judgeId && { judgeId: params.judgeId }),
        ...(params.candidateId && { candidateId: params.candidateId }),
        ...(params.criterionId && { criterionId: params.criterionId }),
        ...(params.segmentId && { segmentId: params.segmentId }),
      }
      console.log('GETTING SCRORES: ', parameters)
      try {
        const response = await api.get('/scores', { params: parameters })
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
    staleTime: 1000 * 60 * 10,
    enabled,
  })
}
