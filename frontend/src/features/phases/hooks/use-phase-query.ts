import { useQuery } from '@tanstack/react-query'
import { phaseDetailedSchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useOngoingPhaseQuery(id: string | null | undefined) {
  return useQuery({
    queryKey: ['phases', id],
    queryFn: async () => {
      try {
        const response = await api.get(`/phases/${id}`)
        const parsedResponse = phaseDetailedSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error(`/phases/${id} response doesn't match schema!`)
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
    enabled: !!id,
    staleTime: 1000 * 60 * 10,
  })
}
