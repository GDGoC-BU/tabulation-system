import axios from 'axios'

const api = axios.create({
  baseURL: `http://${process.env.NEXT_PUBLIC_BACKEND_HOST}:${process.env.NEXT_PUBLIC_BACKEND_PORT}/api/v1`,
  headers: {
    'Content-Type': 'application/json'
  },
  timeout: 10000
})

// api.interceptors.request.use(async (config) => {
// 	const provider = useAuthentication.getState().provider;

// 	if (provider && provider.authenticated) {
// 		config.headers.Authorization = `Bearer ${provider.token}`;
// 	}

// 	return config;
// });

export default api
