import { useQuery } from '@tanstack/react-query'
import { phasesSchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function usePhasesQuery() {
  return useQuery({
    queryKey: ['phases'],
    queryFn: async () => {
      try {
        const response = await api.get('/phases')
        const parsedResponse = phasesSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          console.error("/phases response doesn't match schema!")
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
