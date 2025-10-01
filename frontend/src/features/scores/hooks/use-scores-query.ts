import { useQuery } from '@tanstack/react-query'
import { scoreDetailedSchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useScoresQuery({
  judgeId,
  candidateId,
  criterionId,
  segmentId,
}: {
  judgeId?: string
  candidateId?: string
  criterionId?: string
  segmentId?: string
}) {
  return useQuery({
    queryKey: ['scores'],
    queryFn: async () => {
      const params = {
        ...(judgeId && { judgeId }),
        ...(candidateId && { candidateId }),
        ...(criterionId && { criterionId }),
        ...(segmentId && { segmentId }),
      }

      try {
        const response = await api.get('/scores', { params })
        const parsedResponse = scoreDetailedSchema.safeParse(response.data)
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
  })
}
