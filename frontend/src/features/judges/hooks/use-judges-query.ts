import { useQuery } from '@tanstack/react-query'
import { judgesSchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useJudgesQuery() {
  return useQuery({
    queryKey: ['judges'],
    queryFn: async () => {
      try {
        const response = await api.get('/judges')
        const parsedResponse = judgesSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          console.error("/judges response doesn't match schema!")
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
