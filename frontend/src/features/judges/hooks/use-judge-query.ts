import { useQuery } from '@tanstack/react-query'
import { judgeSummarySchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useJudgeQuery(id: string | null | undefined) {
  return useQuery({
    queryKey: ['judges', id],
    queryFn: async () => {
      try {
        const response = await api.get(`/judges/${id}`)
        const parsedResponse = judgeSummarySchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error(`/judges/id response doesn't match schema!`)
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
