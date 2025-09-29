import { useQuery } from '@tanstack/react-query'
import { phaseDetailedSchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useOngoingPhaseQuery() {
  return useQuery({
    queryKey: ['phases', 'ongoing'],
    queryFn: async () => {
      try {
        const response = await api.get('/phases/ongoing')
        const parsedResponse = phaseDetailedSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error("/phases/ongoing response doesn't match schema!")
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
    staleTime: 1000 * 60 * 10,
  })
}
