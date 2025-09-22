'use server'

import { BackendErrorResponse, ServerFormActionResponse } from '@/types'
import { pageantEditSchema } from '../schemas/pageant-edit'
import api from '@/lib/axios'
import axios from 'axios'

export async function editPageant(
  data: unknown
): Promise<ServerFormActionResponse> {
  const result = pageantEditSchema.safeParse(data)
  if (!result.success) {
    return {
      isSuccessful: false,
      message: 'Invalid inputs!'
    }
  }

  const id = result.data.id
  const body = {
    title: result.data.title
  }

  try {
    const response = await api.put(`/pageants/${id}`, body)
    console.log(response)
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
