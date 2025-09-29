import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { jwtDecode } from 'jwt-decode'
import type { AccountRole, AccountStore } from '../schemas'
import type { BackendJwtPayload } from '@/types'

export type AuthenticationStore = {
  account: AccountStore | null
  login: (token: string) => void
  logout: () => void
  isAuthenticated: () => boolean
  getAccountRole: () => AccountRole | null
  getAccountId: () => string | null
}

export const useAuthenticationStore = create<AuthenticationStore>()(
  persist(
    (set, get) => ({
      account: null,

      login: (token: string) => {
        try {
          const { sub, role, account_id, assigned_pageant_id } =
            jwtDecode<BackendJwtPayload>(token)
          const account: AccountStore = {
            id: account_id,
            username: sub,
            assigned_pageant_id: assigned_pageant_id
              ? assigned_pageant_id
              : null,
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

      getAccountRole: () => {
        const account = get().account
        return account ? account.role : null
      },

      getAccountId: () => {
        const account = get().account
        return account ? account.id : null
      },
    }),
    {
      name: 'AUTHENTICATION',
      partialize: (state) => ({ account: state.account }),
    },
  ),
)
