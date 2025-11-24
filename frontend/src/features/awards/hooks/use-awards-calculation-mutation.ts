import { useQueries } from '@tanstack/react-query'
import { awardDetailedSchema } from '../schemas'
import type { Awards } from '../schemas'
import errorResolver from '@/lib/error-resolver'
import api from '@/lib/axios'

export function useAwardsCalculation(awards?: Awards) {
  const queries = useQueries({
    queries: (awards ?? []).map((award) => ({
      queryKey: ['award', award.id, 'calculate'],
      queryFn: async () => {
        try {
          const response = await api.post(`/awards/${award.id}/calculate`)
          const parsedResponse = awardDetailedSchema.safeParse(response.data)
          if (!parsedResponse.success) {
            throw new Error(
              `/awards/${award.id} response doesn't match schema!`,
            )
          }

          const sortedLeaderboard = [...parsedResponse.data.leaderboard].sort(
            (a, b) => b.score - a.score,
          )

          return { ...parsedResponse.data, leaderboard: sortedLeaderboard }
        } catch (error) {
          throw errorResolver(error)
        }
      },
      enabled: !!award.id,
      staleTime: 1000 * 60 * 10,
    })),
  })

  const results = queries.map((query, i) => ({
    data: query.data,
    isLoading: query.isLoading,
    isError: query.isError,
  }))

  return results
}
