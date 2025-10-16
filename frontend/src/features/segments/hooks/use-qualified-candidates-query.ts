import { useQuery } from '@tanstack/react-query'
import { segmentDetailedSchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useQualifiedCandidates(id: string | null | undefined) {
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
            `/segments/${id}/calculate-qualified-candidates response doesn't match schema!`,
          )
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
    enabled: !!id,
  })
}
