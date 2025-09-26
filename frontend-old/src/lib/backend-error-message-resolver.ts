import axios from 'axios'
import { BackendErrorResponse } from '@/types'

export function backendErrorMessageResolver(error: unknown): string {
  if (axios.isAxiosError(error)) {
    if (error.response) {
      const backendError = error.response.data as BackendErrorResponse
      return backendError.message
    }
    return 'Something went wrong! Please contact admin.'
  }
  return 'Unexpected error occurred.'
}
