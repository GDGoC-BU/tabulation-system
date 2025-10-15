import { useQuery } from '@tanstack/react-query'
import { segmentDetailedSchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useOngoingSegmentQuery() {
  return useQuery({
    queryKey: ['segments', 'ongoing'],
    queryFn: async () => {
      try {
        const response = await api.get('/segments/ongoing')
        const parsedResponse = segmentDetailedSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error("/segments/ongoing response doesn't match schema!")
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
    // staleTime: 1000 * 60 * 10,
  })
}
