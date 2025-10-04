import { useQuery } from '@tanstack/react-query'
import { pageantHierarchySchema } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function usePageantHierarchyQuery(id: string | null | undefined) {
  return useQuery({
    queryKey: ['pageants', id, 'hierarchy'],
    queryFn: async () => {
      try {
        const response = await api.get(`/pageants/${id}/hierarchy`)
        console.log('res: ', response.data)
        const parsedResponse = pageantHierarchySchema.safeParse(response.data)
        if (!parsedResponse.success) {
          throw new Error(
            `/pageant/id/hierarchy response doesn't match schema!`,
          )
        }
        console.log('parsed: ', parsedResponse.data)
        return parsedResponse.data
      } catch (error) {
        console.log(error)
        throw errorResolver(error)
      }
    },
    enabled: !!id,
    staleTime: 1000 * 60 * 10,
  })
}
