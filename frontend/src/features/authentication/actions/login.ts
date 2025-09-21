'use server'

import api from '@/lib/axios'
import loginSchema from '../schemas/login'
import { cookies } from 'next/headers'
import { redirect } from 'next/navigation'
import axios from 'axios'

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
    const response = await api.post('/accounts/login', body)
    const token = response.data

    const cookieStore = await cookies()
    cookieStore.set({
      name: 'token',
      value: token,
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
        return {
          isSuccessful: false,
          message: 'Wrong username or password!'
        }
      }
      return {
        isSuccessful: false,
        message: 'Something went wrong! Please contact admin.'
      }
    }
  }
}
