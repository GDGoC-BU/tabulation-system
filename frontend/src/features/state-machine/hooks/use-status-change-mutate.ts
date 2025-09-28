import { useMutation } from '@tanstack/react-query'
import api from '@/lib/axios'

export default function useStatusChangeMutate() {
  return useMutation({
    mutationFn: async (url: string) => {
      await api.post(url)
    },
  })
}
