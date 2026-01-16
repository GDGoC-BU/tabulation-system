import { useMutation } from '@tanstack/react-query'
import type { SegmentEditForm } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function useEditSegmentMutate() {
  return useMutation<boolean, string, SegmentEditForm>({
    mutationFn: async (data) => {
      try {
        const body = {
          name: data.name,
          qualificationLeaderboard: data.qualificationLeaderboard,
        }
        await api.put(`/segments/${data.id}`, body)
        return true
      } catch (error) {
        throw errorResolver(error)
      }
    },
    onError: (error) => {
      console.error(`Editing segment failed: `, error)
    },
  })
}
