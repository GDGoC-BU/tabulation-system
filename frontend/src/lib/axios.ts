import axios from 'axios'
import { useAuthentication } from '@/features/authentication/store/use-authentication'

const api = axios.create({
  baseURL: `http://${import.meta.env.VITE_BACKEND_HOST}:${import.meta.env.VITE_BACKEND_PORT}/api/v1`,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
})

api.interceptors.request.use((config) => {
  const { account, isAuthenticated } = useAuthentication.getState()
  if (isAuthenticated()) {
    config.headers.Authorization = `Bearer ${account?.token}`
  }
  return config
})

export default api
