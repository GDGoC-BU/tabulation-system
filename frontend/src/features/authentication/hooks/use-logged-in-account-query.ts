import { useQuery } from '@tanstack/react-query'
import { accountSummarySchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useLoggedInAccountQuery() {
  return useQuery({
    queryKey: ['accounts/me'],
    queryFn: async () => {
      try {
        const response = await api.get('/accounts/me')
        console.log(response.data)
        const parsedResponse = accountSummarySchema.safeParse(response.data)
        if (!parsedResponse.success) {
          console.error("/accounts/me response doesn't match schema!")
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
