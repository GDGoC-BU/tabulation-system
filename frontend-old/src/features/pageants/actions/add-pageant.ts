'use server'

import { BackendErrorResponse, ServerFormActionResponse } from '@/types'
import { pageantAddSchema } from '../schemas'
import api from '@/lib/axios'
import axios from 'axios'

export async function addPageant(
  data: unknown
): Promise<ServerFormActionResponse> {
  const result = pageantAddSchema.safeParse(data)
  if (!result.success) {
    return {
      isSuccessful: false,
      message: 'Invalid inputs!'
    }
  }

  const body = {
    title: result.data.title
  }

  try {
    const response = await api.post(`/pageants`, body)
    return {
      isSuccessful: true
    }
  } catch (error: unknown) {
    if (axios.isAxiosError(error)) {
      if (error.response) {
        const backendError = error.response.data as BackendErrorResponse
        return {
          isSuccessful: false,
          message: backendError.message
        }
      }
      return {
        isSuccessful: false,
        message: 'Something went wrong! Please contact admin.'
      }
    }
  }
  return {
    isSuccessful: false,
    message: 'something went wrong'
  }
}
