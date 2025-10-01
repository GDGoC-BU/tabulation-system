import { useQuery } from '@tanstack/react-query'
import { candidatesSchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useCandidatesQuery() {
  return useQuery({
    queryKey: ['candidates'],
    queryFn: async () => {
      try {
        const response = await api.get('/candidates')
        const parsedResponse = candidatesSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          console.error("/candidates response doesn't match schema!")
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
