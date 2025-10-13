import { useQuery } from '@tanstack/react-query'
import { awardDetailedSchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useAwardQuery(id: string | null | undefined) {
  return useQuery({
    queryKey: ['awards', id],
    queryFn: async () => {
      try {
        const response = await api.get(`/awards/${id}`)
        const parsedResponse = awardDetailedSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error(`/awards/${id} response doesn't match schema!`)
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
