import { useMutation } from '@tanstack/react-query'
import { pageantSummarySchema } from '../schemas'
import type { PageantSummary } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useSoftResetMutate(id: string | null | undefined) {
  return useMutation<PageantSummary, string, string | null | undefined>({
    mutationFn: async () => {
      try {
        const response = await api.put(`/pageants/${id}/soft-reset`)
        console.log('res: ', response)
        const parsedResponse = pageantSummarySchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error(
            `/pageants/${id}/soft-reset response doesn't match schema!`,
          )
        }
        return parsedResponse.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
  })
}
