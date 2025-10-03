import { useQuery } from '@tanstack/react-query'
import { awardsSchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useAwardsQuery() {
  return useQuery({
    queryKey: ['awards'],
    queryFn: async () => {
      try {
        const response = await api.get('/awards')
        const parsedResponse = awardsSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          console.error("/awards response doesn't match schema!")
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
