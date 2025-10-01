import axios from 'axios'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'
import { useSelectedPageantIdStore } from '@/features/pageants/store/use-selected-pageant-id-store'

const api = axios.create({
  baseURL: `http://${import.meta.env.VITE_BACKEND_HOST}:${import.meta.env.VITE_BACKEND_PORT}/api/v1`,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
})

/* Add token to authenticate request */
api.interceptors.request.use((config) => {
  const { account, isAuthenticated } = useAuthenticationStore.getState()
  if (isAuthenticated()) {
    config.headers.Authorization = `Bearer ${account?.token}`
  }
  return config
})

/* Add pageant id, if available, to the request header  */
api.interceptors.request.use((config) => {
  const { selectedPageantId: id } = useSelectedPageantIdStore.getState()
  if (!id) {
    return config
  }
  config.headers['Pageant-Id'] = id
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      const { logout } = useAuthenticationStore.getState()
      logout()
      /* Hard reload after logout to trigger the layout
         protection, and clear the stale stores */
      window.location.reload()
    }
    return Promise.reject(error)
  },
)

export default api
