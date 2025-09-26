import { useQuery } from '@tanstack/react-query'
import { collegesSchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useCollegesQuery() {
  return useQuery({
    queryKey: ['colleges'],
    queryFn: async () => {
      try {
        const response = await api.get('/colleges')
        const parsedResponse = collegesSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          console.error("/colleges response doesn't match schema!")
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
