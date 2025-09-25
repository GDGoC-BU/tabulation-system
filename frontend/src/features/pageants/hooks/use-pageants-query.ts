import { useQuery } from '@tanstack/react-query'
import { pageantsSchema } from '../schemas'
import type { Pageants } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function usePageantsQuery() {
  return useQuery<Pageants>({
    queryKey: ['pageants'],
    queryFn: async () => {
      try {
        const response = await api.get('/pageants')
        const parsedResponse = pageantsSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          console.error("/pageants response doesn't match schema!")
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
