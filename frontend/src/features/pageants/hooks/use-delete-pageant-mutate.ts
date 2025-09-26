import { useMutation } from '@tanstack/react-query'
import type { pageantDeleteSchema } from '../schemas'
import type z from 'zod'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export default function useDeletePageantMutate() {
  return useMutation<boolean, string, z.infer<typeof pageantDeleteSchema>>({
    mutationFn: async (data) => {
      try {
        await api.delete(`/pageants/${data.id}`)
        return true
      } catch (error) {
        throw errorResolver(error)
      }
    },
    onError: (error) => {
      console.error('Deleting pageant failed', error)
    },
  })
}
