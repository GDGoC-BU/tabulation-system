'use server'

import api from '@/lib/axios'
import loginSchema from '../schemas/login'
import { cookies } from 'next/headers'
import axios from 'axios'
import {
  BackendErrorResponse,
  ServerFormActionResponse,
  BackendJwtPayload
} from '@/types'
import { jwtDecode } from 'jwt-decode'

export async function login(data: unknown): Promise<ServerFormActionResponse> {
  const result = loginSchema.safeParse(data)
  if (!result.success) {
    return {
      isSuccessful: false,
      message: 'Invalid inputs!'
    }
  }

  const body = {
    username: result.data.username,
    password: result.data.password
  }

  try {
    /* Delete cookie so that no token in the auth header is present */
    const cookieStore = await cookies()
    cookieStore.delete('TOKEN')

    /* login with credentials in body */
    const response = await api.post('/accounts/login', body)
    const token = response.data
    const { exp } = jwtDecode<BackendJwtPayload>(token)

    if (!exp) {
      return {
        isSuccessful: false,
        message: 'No expiry date in token.'
      }
    }

    cookieStore.set({
      name: 'TOKEN',
      value: token,
      expires: new Date(exp * 1000),
      secure: true,
      httpOnly: true,
      path: '/',
      sameSite: 'strict'
    })

    return {
      isSuccessful: true
    }
  } catch (error: unknown) {
    if (axios.isAxiosError(error)) {
      if (error.response) {
        const backendError = error.response.data as BackendErrorResponse
        console.log('Backend: ', backendError)
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
