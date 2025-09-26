import { RouterProvider, createRouter } from '@tanstack/react-router'

import * as TanStackQueryProvider from './integrations/tanstack-query/root-provider.tsx'

// Import the generated route tree
import { routeTree } from './routeTree.gen'

import './styles.css'
import { useAuthenticationStore } from './features/authentication/store/use-authentication-store.ts'

const TanStackQueryProviderContext = TanStackQueryProvider.getContext()
const router = createRouter({
  routeTree,
  context: {
    ...TanStackQueryProviderContext,
    authentication: undefined!,
  },
  defaultPreload: 'intent',
  scrollRestoration: true,
  defaultStructuralSharing: true,
  defaultPreloadStaleTime: 0,
})

// Register the router instance for type safety
declare module '@tanstack/react-router' {
  interface Register {
    router: typeof router
  }
}

function App() {
  const authentication = useAuthenticationStore()

  return (
    <TanStackQueryProvider.Provider {...TanStackQueryProviderContext}>
      <RouterProvider router={router} context={{ authentication }} />
    </TanStackQueryProvider.Provider>
  )
}

export default App
