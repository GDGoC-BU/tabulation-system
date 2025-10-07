import { useMutation } from '@tanstack/react-query'
import type { AwardAddForm } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function useAddAwardMutate() {
  return useMutation<boolean, string, AwardAddForm>({
    mutationFn: async (data) => {
      try {
        const body = {
          name: data.name,
          candidateLimit: data.candidateLimit,
          formula: data.formula,
        }
        await api.post(`/awards`, body)
        return true
      } catch (error) {
        throw errorResolver(error)
      }
    },
    onError: (error) => {
      console.error('Adding award failed', error)
    },
  })
}
