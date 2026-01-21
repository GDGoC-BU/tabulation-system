import { queryOptions } from '@tanstack/react-query'
import type { UseQueryOptions } from '@tanstack/react-query'
import type { LeaderboardDetailed } from '@/features/leaderboard/schemas'
import { leaderboardDetailedSchema } from '@/features/leaderboard/schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function segmentQualificationLeaderboardQueryOptions<
  TData = LeaderboardDetailed | null,
  TError = string,
>(
  id: string | undefined | null,
  options?: Omit<
    UseQueryOptions<LeaderboardDetailed | null, TError, TData>,
    'queryKey' | 'queryFn'
  >,
) {
  return queryOptions({
    enabled: !!id,
    staleTime: 1000 * 60 * 10,
    ...options,
    queryKey: ['segments', id, 'qualificationLeaderboard'],
    queryFn: async () => {
      try {
        const response = await api.get(
          `/segments/${id}/qualificationLeaderboard`,
        )
        const parsedResponse = leaderboardDetailedSchema.safeParse(
          response.data,
        )
        console.log('Backend called. IsSuccess: ', parsedResponse.success)
        console.log('Error: ', parsedResponse.error)
        if (!parsedResponse.success) {
          throw new Error(
            `/segments/${id}/qualificationLeaderboard response doesn't match schema!`,
          )
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
  })
}
