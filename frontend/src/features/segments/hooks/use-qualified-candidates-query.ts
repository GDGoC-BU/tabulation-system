import { useQuery } from '@tanstack/react-query'
import { segmentDetailedSchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useSegmentCalculateQualifiedCandidates(
  id: string | null | undefined,
) {
  return useQuery({
    queryKey: ['segments', id],
    queryFn: async () => {
      try {
        const response = await api.post(
          `/segments/${id}/calculate-qualified-candidates`,
        )
        const parsedResponse = segmentDetailedSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error(
            `"/segments/${id}/calculate-qualified-candidates" response doesn't match schema!`,
          )
        }

        const sortedCandidateQualifications = [
          ...parsedResponse.data.candidateQualifications,
        ].sort((a, b) => b.score - a.score)

        return {
          ...parsedResponse.data,
          candidateQualifications: sortedCandidateQualifications,
        }
      } catch (error) {
        throw errorResolver(error)
      }
    },
    enabled: !!id,
  })
}
