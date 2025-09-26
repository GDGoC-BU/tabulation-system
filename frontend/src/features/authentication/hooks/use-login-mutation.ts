import { useMutation } from '@tanstack/react-query'
import type { LoginParameters } from '../schemas'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useLoginMutation() {
  return useMutation<string, string, LoginParameters>({
    mutationFn: async (values) => {
      try {
        const res = await api.post('/accounts/login', values)
        return res.data
      } catch (error) {
        throw errorResolver(error)
      }
    },
    onError: (error) => {
      console.error('Login failed', error)
    },
  })
}
