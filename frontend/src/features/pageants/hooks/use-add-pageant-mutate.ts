import { useMutation } from '@tanstack/react-query'
import type { PageantAddParameters } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function useAddPageantMutate() {
  return useMutation<boolean, string, PageantAddParameters>({
    mutationFn: async (data) => {
      try {
        const body = {
          title: data.title,
        }
        await api.post(`/pageants`, body)
        return true
      } catch (error) {
        throw errorResolver(error)
      }
    },
    onError: (error) => {
      console.error('Adding pageant failed', error)
    },
  })
}
