import { useQuery } from '@tanstack/react-query'
import { awardDetailedSchema } from '../schemas'
import errorResolver from '@/lib/error-resolver'
import api from '@/lib/axios'

export function useAwardCalculation(id: string | undefined) {
  return useQuery({
    queryKey: ['award', id, 'calculate'],
    queryFn: async () => {
      if (!id) return null

      try {
        const response = await api.post(`/awards/${id}/calculate`)
        const parsedResponse = awardDetailedSchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error(`/awards/${id} response doesn't match schema!`)
        }

        const sortedLeaderboard = [...parsedResponse.data.leaderboard].sort(
          (a, b) => b.score - a.score,
        )

        return { ...parsedResponse.data, leaderboard: sortedLeaderboard }
      } catch (error) {
        throw errorResolver(error)
      }
    },
    enabled: !!id,
    staleTime: 1000 * 60 * 10,
  })
}
