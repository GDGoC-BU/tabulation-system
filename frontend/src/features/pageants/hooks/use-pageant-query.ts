import { useQuery } from '@tanstack/react-query'
import { pageantSummarySchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function usePageantQuery(id: string) {
  return useQuery({
    queryKey: ['pageants', id],
    queryFn: async () => {
      try {
        const response = await api.get(`/pageants/${id}`)
        const parsedResponse = pageantSummarySchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error(`/pageant/id response doesn't match schema!`)
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
