import axios from 'axios'

export default function errorResolver(error: unknown): string {
  if (axios.isAxiosError(error)) {
    // If response is the custom backend error response
    const backendError = error.response?.data
    if (backendError && typeof backendError.message === 'string') {
      return backendError.message
    }
    // If no backend error response, default to axios' provided error  message (network errors, timeouts, etc)
    if (error.message) {
      return error.message
    }
  }

  // Generic Error was thrown
  if (error instanceof Error) {
    return error.message
  }
  // Extreme case where error is none of the above
  return 'Something went wrong! Contact admin.'
}
