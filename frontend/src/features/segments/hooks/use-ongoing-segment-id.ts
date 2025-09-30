import { useEffect, useState } from 'react'
import { useStompStore } from '@/store/stomp-store'

export function useOngoingSegmentId(pageantId?: string) {
  const [ongoingSegmentId, setOngoingSegmentId] = useState<string | null>(null)

  useEffect(() => {
    if (!pageantId) return

    const { subscribe } = useStompStore.getState()

    const subscription = subscribe(
      `/topic/pageants/${pageantId}/ongoing-segment`,
      (message) => {
        try {
          const data = JSON.parse(message.body)
          console.log('Ongoing segment:', data)
          setOngoingSegmentId(data?.id ?? null)
        } catch (err) {
          console.error('Failed to parse STOMP message', err)
        }
      },
    )

    return () => {
      subscription?.unsubscribe()
    }
  }, [pageantId])

  return ongoingSegmentId
}
