'use server'

import { ServerFormActionResponse } from '@/types'
import { pageantEditSchema } from '../schemas'
import api from '@/lib/axios'
import { backendErrorMessageResolver } from '@/lib/backend-error-message-resolver'
import { redirect } from 'next/navigation'
import { UnauthorizedError } from '@/lib/UnauthorizedError'

export async function editPageant(
  data: unknown
): Promise<ServerFormActionResponse> {
  let toRedirect = false
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
    return {
      isSuccessful: true
    }
  } catch (error) {
    if (error instanceof UnauthorizedError) {
      toRedirect = true
    }

    if (!toRedirect) {
      return {
        isSuccessful: false,
        message: backendErrorMessageResolver(error)
      }
    }
  }

  if (toRedirect) {
    redirect('/admin/login')
  } else {
    return {
      isSuccessful: false,
      message: 'Return error to satisy TS'
    }
  }
}
