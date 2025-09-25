import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { jwtDecode } from 'jwt-decode'
import type { Account } from '../schemas'
import type { BackendJwtPayload } from '@/types'

export type AuthenticationStore = {
  account: Account | null
  setAuthentication: (token: string) => void
  logout: () => void
  isAuthenticated: () => boolean
}

export const useAuthentication = create<AuthenticationStore>()(
  persist(
    (set, get) => ({
      account: null,

      setAuthentication: (token: string) => {
        try {
          const { sub, role } = jwtDecode<BackendJwtPayload>(token)
          const account: Account = {
            username: sub,
            role,
            token,
          }
          set({ account })
        } catch (e) {
          console.error('Invalid token:', e)
          set({ account: null })
        }
      },

      logout: () => {
        set({ account: null })
      },

      isAuthenticated: () => {
        return get().account !== null
      },
    }),
    {
      name: 'AUTHENTICATION_STORAGE',
      partialize: (state) => ({ account: state.account }),
    },
  ),
)
