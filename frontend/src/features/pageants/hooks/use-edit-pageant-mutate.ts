import { useMutation } from '@tanstack/react-query'
import type { PageantEditParameters } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function useEditPageantMutate() {
  return useMutation<boolean, string, PageantEditParameters>({
    mutationFn: async (data) => {
      try {
        const body = {
          title: data.title,
        }
        await api.put(`/pageants/${data.id}`, body)
        return true
      } catch (error) {
        throw errorResolver(error)
      }
    },
    onError: (error) => {
      console.error('Editing pageant failed', error)
    },
  })
}
