'use server'

import api from '@/lib/axios'
import { collegesSchema } from '../schemas'

export async function getColleges() {
  try {
    const response = await api.get('/colleges')
    const parsedResponse = collegesSchema.safeParse(response.data)
    if (!parsedResponse.success) {
      return []
    }
    return parsedResponse.data
  } catch (error) {
    console.log('Error fetching colleges: ', error)
  }
  return []
}
