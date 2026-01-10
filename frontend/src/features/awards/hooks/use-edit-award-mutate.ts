import { useMutation } from '@tanstack/react-query'
import type { AwardEditForm } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function useEditAwardMutate() {
  return useMutation<boolean, string, AwardEditForm>({
    mutationFn: async (data) => {
      try {
        const body = {
          name: data.name,
          candidateLimit: data.candidateLimit,
          formula: data.formula,
        }
        await api.put(`/awards/${data.id}`, body)
        return true
      } catch (error) {
        throw errorResolver(error)
      }
    },
    onError: (error) => {
      console.error('Editing award failed: ', error)
    },
  })
}
