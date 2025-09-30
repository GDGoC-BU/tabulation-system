import SockJS from 'sockjs-client'
import { create } from 'zustand'
import { Client } from '@stomp/stompjs'
import type { IMessage, StompSubscription } from '@stomp/stompjs'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'

export type StompStore = {
  client: Client | null
  connect: () => void
  disconnect: () => void
  subscribe: (
    topic: string,
    callback: (message: IMessage) => void,
  ) => StompSubscription | null
}

export const useStompStore = create<StompStore>((set, get) => ({
  client: null,

  connect: () => {
    if (get().client) return

    /* Reject connection if not authenticated. We need the token to make
       requests to the protected backend. */
    const { account, isAuthenticated } = useAuthenticationStore.getState()
    if (!isAuthenticated()) {
      return
    }

    /* Create client */
    const client = new Client({
      webSocketFactory: () =>
        new SockJS(
          `http://${import.meta.env.VITE_BACKEND_HOST}:${import.meta.env.VITE_BACKEND_PORT}/ws`,
        ),
      reconnectDelay: 5000,
      debug: (str) => console.log('[STOMP]', str),
      connectHeaders: {
        Authorization: `Bearer ${account?.token}`,
      },
    })

    client.onConnect = () => {
      console.log('Connected to STOMP broker...')
    }

    client.onStompError = (frame) => {
      console.error('STOMP connection error...', frame)
    }

    client.activate()
    set({ client })
  },

  disconnect: () => {
    get().client?.deactivate()
    set({ client: null })
  },

  subscribe: (topic, callback) => {
    const client = get().client
    if (!client) {
      console.warn('No STOMP client available...')
      return null
    }

    if (client.connected) {
      return client.subscribe(topic, callback)
    }

    /* If not yet connected. Wait for the handshake to fulfill. */
    let subscription: StompSubscription | null = null
    client.onConnect = () => {
      subscription = client.subscribe(topic, callback)
    }
    return subscription
  },
}))
