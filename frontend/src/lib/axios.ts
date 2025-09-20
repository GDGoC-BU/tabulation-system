import axios from 'axios'

const api = axios.create({
  baseURL: `http://${process.env.BACKEND_HOST}:${process.env.BACKEND_PORT}`,
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
