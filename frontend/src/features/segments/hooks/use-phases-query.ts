import { useQuery } from '@tanstack/react-query'
import { segmentsSchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useSegmentsQuery() {
  return useQuery({
    queryKey: ['segments'],
    queryFn: async () => {
      try {
        const response = await api.get('/segments')
        console.log(response.data)
        const parsedResponse = segmentsSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          console.error("/segments response doesn't match schema!")
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
