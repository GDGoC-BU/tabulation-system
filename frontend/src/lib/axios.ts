import axios from 'axios'

const api = axios.create({
  baseURL: `http://${import.meta.env.VITE_BACKEND_HOST}:${import.meta.env.VITE_BACKEND_PORT}/api/v1`,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
})

// api.interceptors.request.use(async (config) => {
//   const cookieStore = await cookies()
//   const tokenCookie = cookieStore.get('TOKEN')
//   const token = tokenCookie?.value

//   if (token) {
//     config.headers.Authorization = `Bearer ${token}`
//   }

//   return config
// })

export default api
