import { useMutation } from '@tanstack/react-query'
import type { ScoreDetailed, ScoreEditForm } from '../schemas/index'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function useEditScoreMutate() {
  return useMutation<ScoreDetailed, string, ScoreEditForm>({
    mutationFn: async (data) => {
      try {
        const body = {
          value: data.value,
        }
        const response = await api.put(`/scores/${data.id}`, body)
        return response.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
    onError: (error) => {
      console.error('Editing score failed', error)
    },
  })
}
