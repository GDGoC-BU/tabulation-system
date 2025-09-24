'use server'

import api from '@/lib/axios'
import { pageantsSchema } from '../schemas/pageant'

export async function getPageants() {
  try {
    const response = await api.get('/pageants')
    const parsedResponse = pageantsSchema.safeParse(response.data)
    if (!parsedResponse.success) {
      return []
    }
    return parsedResponse.data
  } catch (error) {
    console.log('Error fetching pageants: ', error)
  }
  return []
}
