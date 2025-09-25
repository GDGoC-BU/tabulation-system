import { useMutation } from '@tanstack/react-query'
import type loginSchema from '../schemas'
import type z from 'zod'
import api from '@/lib/axios'
import errorResolver from '@/lib/error-resolver'

export function useLoginMutation() {
  return useMutation<string, string, z.infer<typeof loginSchema>>({
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
