import axios from 'axios'
import { useAuthentication } from '@/features/authentication/store/use-authentication'
import { useSelectedPageantId } from '@/features/pageants/store/use-selected-pageant-id'

const api = axios.create({
  baseURL: `http://${import.meta.env.VITE_BACKEND_HOST}:${import.meta.env.VITE_BACKEND_PORT}/api/v1`,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
})

/* Add token to authenticate request */
api.interceptors.request.use((config) => {
  const { account, isAuthenticated } = useAuthentication.getState()
  if (isAuthenticated()) {
    config.headers.Authorization = `Bearer ${account?.token}`
  }
  return config
})

/* Add pageant id, if available, to the request header  */
api.interceptors.request.use((config) => {
  const { selectedPageantId: id } = useSelectedPageantId.getState()
  if (!id) {
    return config
  }
  config.headers['Pageant-Id'] = id
  return config
})

export default api
